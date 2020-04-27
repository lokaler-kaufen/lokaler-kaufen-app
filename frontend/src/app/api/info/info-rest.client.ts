import {Injectable} from '@angular/core';
import {VersionDto} from '../../data/client/model/versionDto';
import {HttpClient} from '@angular/common/http';
import {wrapRequest} from '../api-utilities';

const API = '/api/info/version';

@Injectable({providedIn: 'root'})
export class InfoRestClient {

  constructor(private http: HttpClient) {
  }

  version(): Promise<VersionDto> {
    return wrapRequest(
      this.http.get(API),
      '[InfoRestClient] failed to retrieve version info'
    );
  }

}
