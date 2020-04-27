import {ClientErrorDto} from '../data/client/model/clientErrorDto';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ApiClientBase} from './api-client-base.service';
import {Observable} from 'rxjs';

const API = '/api/client-error';

@Injectable({providedIn: 'root'})
export class ClientErrorClient {

  constructor(private http: HttpClient, private base: ApiClientBase) {
  }

  reportError(clientErrorDto: ClientErrorDto): Promise<void> {
    return this.base.promisify<void>(
      this.http.post(API, clientErrorDto) as Observable<any>,
      '[ClientErrorClient] failed to upload client error'
    );
  }

}
