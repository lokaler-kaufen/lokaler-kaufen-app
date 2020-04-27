import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpEventType} from '@angular/common/http';
import {ImageDto} from '../data/api';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {ApiClientBase} from './api-client-base.service';

const API_IMAGE_SHOP = '/api/image/shop';

type ProgressConsumer = (progress: number) => void;

@Injectable({providedIn: 'root'})
export class ShopImageClient {

  constructor(private client: HttpClient, private base: ApiClientBase) {
  }

  /**
   * Issues shop logo deletion to the backend.
   */
  deleteImageFromShop(): Promise<void> {
    return this.base.promisify(
      this.client.delete<void>(API_IMAGE_SHOP),
      '[ShopImageClient.upload] failed to delete shop logo.'
    );
  }

  /**
   * Uploads the given image file.
   *
   * @param image File object holding the image.
   * @param onProgress (Optional) callback that will get called with the progress (in flat percent) during the upload.
   * @return A promise that resolves to an {@link ImageDto} on success.
   */
  uploadImageForShop(image: File, onProgress?: ProgressConsumer): Promise<ImageDto> {
    return this.doUpload(image)
      .pipe(
        // handle progress and response
        map(this.handleUploadProgressAndResponse(onProgress)),

        // log errors but still have the promise reject down the road
        catchError(this.logUploadError(image.name))
      )
      .toPromise();
  }

  private doUpload(image: File): Observable<HttpEvent<ImageDto>> {
    const formData = new FormData();
    formData.append('file', image, image.name);

    return this.client.post<ImageDto>(API_IMAGE_SHOP, formData, {
      reportProgress: true,
      observe: 'events'
    });
  }

  private handleUploadProgressAndResponse(onProgress?: (progress: number) => void) {
    return event => {
      switch (event.type) {
        case HttpEventType.UploadProgress:
          // total MAY not be present in some browsers
          if (onProgress && event.total) {
            const progress = Math.round(event.loaded / event.total) * 100;
            onProgress(progress);
          }
          break;

        case HttpEventType.Response:
          return event.body;
      }
    };
  }

  private logUploadError(name: string) {
    return error => {
      console.error(`[ShopImageClient.upload] failed to upload ${name}.`, error);
      return throwError(error);
    };
  }

}
