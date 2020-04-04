import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  BASE_URL: string = '/api/image/shop';

  constructor(private client: HttpClient) { }

  public upload(formData) {
    return this.client.post<any>(this.BASE_URL + '/upload', formData, {
      reportProgress: true,
      observe: 'events'
    });
  }

}
