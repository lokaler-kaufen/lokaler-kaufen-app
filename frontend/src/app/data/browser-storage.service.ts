import {Inject, Injectable, InjectionToken} from '@angular/core';

/**
 * Injection token for the browser storage to use.
 *
 * The default factory will provide the browser's {@code localStorage} object. You can, however, override it to produce
 * the {@code sessionStorage} (or a mock).
 *
 * See https://angular.io/guide/dependency-injection-in-action for an example.
 */
export const BROWSER_STORAGE = new InjectionToken<Storage>('Browser Storage', {
  providedIn: 'root',
  factory: () => localStorage
});

/**
 * Abstraction of the browser's {@code localStorage} or {@code sessionStorage} APIs.
 *
 * (with some automatic JSON magic inside, as well)
 */
@Injectable({providedIn: 'root'})
export class BrowserStorageService {

  constructor(
    @Inject(BROWSER_STORAGE) public storage: Storage) {
  }

  /**
   * Attempts to look up the value stored under {@code key} in the browser's storage.
   *
   * An attempt will be made to {@code JSON.parse} the value before returning it.
   *
   * @param key Used to look up the desired value from the browser's storage.
   * @return The parsed object that was stored in the browser's storage or {@code null} if no value was found (or
   * something didn't work out retrieving and/or parsing the value).
   */
  get<T>(key: string): T | null {
    try {
      const item = this.storage?.getItem(key);
      return item ? JSON.parse(item) : null;

    } catch (e) {
      console.error(`invalid value stored under ${key}`);
      this.remove(key);
      return null;
    }
  }

  /**
   * Attempts to serialize the given object and put it into the browser's storage under {@code key}.
   *
   * @param key Defines where the given value shall be put in the browser's storage.
   * @param value The object to store.
   */
  set<T>(key: string, value: T) {
    try {
      const item = JSON.stringify(value);
      if (item) {
        this.storage.setItem(key, item);
      }

    } catch (e) {
      console.error(`could not store value under ${key}`);
      this.remove(key);
    }
  }

  /**
   * Attempts to completely remove the given {@code key} from the browser's storage.
   *
   * @param key The key to wipe from the storage.
   */
  remove(key: string) {
    try {
      this.storage.removeItem(key);

    } catch (e) {
      console.error(`could not remove ${key} from storage`);
    }
  }

}
