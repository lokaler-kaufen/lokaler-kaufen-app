import { Injectable } from '@angular/core';

/**
 * Simple caching service that stores the ZIP code in local storage
 */
@Injectable({
  providedIn: 'root'
})
export class ZipCodeCacheService {

  private static readonly KEY = 'zipCode';

  /**
   * Set the zip code (and update it)
   *
   * @param zipCode zipCode that should be stored
   */
  public setZipCode(zipCode: string) {
    localStorage.setItem(ZipCodeCacheService.KEY, zipCode);
  }

  /**
   * Returns the last zip code or null if none is present
   */
  public getZipCode(): string {
    return localStorage.getItem(ZipCodeCacheService.KEY);
  }
}
