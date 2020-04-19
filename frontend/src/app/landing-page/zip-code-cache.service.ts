import {Injectable} from '@angular/core';
import {BrowserStorageService} from '../data/browser-storage.service';

const KEY = 'zipCode';

/**
 * Simple caching service that stores the ZIP code in local storage
 */
@Injectable({providedIn: 'root'})
export class ZipCodeCacheService {

  constructor(private browserStorage: BrowserStorageService) {
  }

  /**
   * Set the zip code (and update it)
   *
   * @param zipCode zipCode that should be stored
   */
  public setZipCode(zipCode: string) {
    this.browserStorage.set(KEY, zipCode);
  }

  /**
   * Returns the last zip code or null if none is present
   */
  public getZipCode(): string | null {
    return this.browserStorage.get(KEY);
  }

}
