import {ClientErrorDto} from '../../data/client/model/clientErrorDto';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {wrapRequest} from '../api-utilities';

const API = '/api/client-error';

@Injectable({providedIn: 'root'})
export class ClientErrorClient {

  constructor(private http: HttpClient) {
  }

  reportError(clientErrorDto: ClientErrorDto): Promise<void> {
    return wrapRequest<void>(
      this.http.post(API, clientErrorDto),
      '[ClientErrorClient] failed to upload client error'
    );
  }

}
