import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {wrapRequest} from '../api-utilities';
import {LocationSuggestionsDto} from '../../data/client/model/locationSuggestionsDto';

const API = '/api/location';

@Injectable({providedIn: 'root'})
export class LocationClient {

  constructor(private http: HttpClient) {
  }

  getSuggestions(zipCode: string): Promise<LocationSuggestionsDto> {
    return wrapRequest(
      this.http.get(`${API}/suggestion`, this.getOptions(zipCode)),
      '[LocationClient] failed to retrieve suggestions'
    );
  }

  isLocationKnown(zipCode: string): Promise<void> {
    return wrapRequest<void>(
      this.http.get(API, this.getOptions(zipCode)),
      '[LocationClient] failed to check known location'
    );
  }

  private getOptions(zipCode: string) {
    return {
      params: new HttpParams().set('zipCode', zipCode)
    };
  }

}
