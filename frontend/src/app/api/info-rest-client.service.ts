import {Injectable} from '@angular/core';
import {VersionDto} from '../data/client/model/versionDto';
import {HttpClient} from '@angular/common/http';
import {ApiClientBase} from './api-client-base.service';

const API = '/api/info/version';

@Injectable({providedIn: 'root'})
export class InfoRestClient {

  constructor(private http: HttpClient, private base: ApiClientBase) {
  }

  version(): Promise<VersionDto> {
    return this.base.promisify(
      this.http.get(API),
      '[InfoRestClient] failed to retrieve version info'
    );
  }

}
