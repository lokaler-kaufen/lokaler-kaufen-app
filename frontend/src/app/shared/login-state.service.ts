import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';

/**
 * Holds the frontend app's assumption about whether the user is logged in as admin or shopowner or not at all.
 *
 * IMPORTANT: This only exists because we have to "write" to this from the {@link LogoutInterceptor} which can't rely
 * on stuff that itself relies on a {@link HttpClient}.
 *
 * => DO NOT INJECT AND USE THIS IN THE APP. NOWHERE.
 *
 * Only the {@link AdminService}, the {@link ShopOwnerService} and the aforementioned {@link LogoutInterceptor} shall
 * use this, period.
 */
@Injectable({providedIn: 'root'})
export class LoginStateService {

  private adminSubject = new Subject<boolean>();
  private shopOwnerSubject = new Subject<boolean>();

  constructor() {
    this.adminSubject.next(false);
    this.shopOwnerSubject.next(false);
  }

  get isAdmin(): Observable<boolean> {
    return this.adminSubject.asObservable();
  }

  loginAdmin() {
    this.adminSubject.next(true);
    this.shopOwnerSubject.next(false);
  }

  logoutAdmin() {
    this.adminSubject.next(false);
  }

  get isShopOwner(): Observable<boolean> {
    return this.shopOwnerSubject.asObservable();
  }

  loginShopOwner() {
    this.shopOwnerSubject.next(true);
  }

  logoutShopOwner() {
    this.shopOwnerSubject.next(false);
  }

}
