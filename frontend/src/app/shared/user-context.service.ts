import { Injectable } from '@angular/core';

/**
 * Simple service that holds the user's context information.
 * Currently only it's login state.
 */
@Injectable({
  providedIn: 'root'
})
export class UserContextService {

  private isLoggedInStoreOwner$ = false;

  constructor() { }

  /**
   * Returns a flag whether the use is a logged in store owner
   */
  get isLoggedInStoreOwner(): boolean {
    return this.isLoggedInStoreOwner$;
  }

  /**
   * Sets the user's login state to true
   */
  public storeOwnerLoggedIn(): void {
    this.isLoggedInStoreOwner$ = true;
  }

  /**
   * Resets state to logged out
   */
  public storeOwnerLoggedOut(): void {
    this.isLoggedInStoreOwner$ = false;
  }
}
