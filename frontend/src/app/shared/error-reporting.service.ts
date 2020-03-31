/* tslint:disable:object-literal-shorthand */
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ErrorReportingService {

  private errorUrl = '/api/client-error';

  constructor(private client: HttpClient) {
  }

  reportError(errorBody: string, requestUrl: string, statusCode: number, traceId: string) {
    this.client.post(this.errorUrl, {
      body: errorBody,
      httpCode: statusCode,
      requestedUrl: requestUrl,
      traceId: traceId
    }).toPromise()
      .then(() => console.log('Client error reporting sent.'))
      .catch(error => console.log('Could not send error reporting.'));
  }

}
