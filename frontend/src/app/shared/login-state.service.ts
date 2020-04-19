import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, OperatorFunction} from 'rxjs';
import {BrowserStorageService} from '../data/browser-storage.service';
import {TokenInfoDto} from '../data/client/model/tokenInfoDto';
import {map} from 'rxjs/operators';

const SHOP_STORAGE_KEY = 'mercury-shop-session';
const ADMIN_STORAGE_KEY = 'mercury-admin-session';

const LOGGED_OUT: TokenInfoDto = {
  status: 'LOGGED_OUT',
  expiry: new Date(0).toISOString()
};

/**
 * Minimum time in ms that has to be available until the stored {@link TokenInfoDto}'s expiry (five minutes).
 */
const EXPIRY_THRESHOLD = 5 * 60 * 1e3;

/**
 * Used to derive the boolean observable from the BehaviorSubject containing a {@link TokenInfoDto}.
 */
const mapToLoggedInBoolean: OperatorFunction<TokenInfoDto, boolean> = map(tokenInfo => {
  return tokenInfo.status === 'LOGGED_IN' && (Date.parse(tokenInfo.expiry) - Date.now() - EXPIRY_THRESHOLD) > 0;
});

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

  private shopOwnerTokenInfo = new BehaviorSubject<TokenInfoDto>(LOGGED_OUT);
  private adminTokenInfo = new BehaviorSubject<TokenInfoDto>(LOGGED_OUT);

  private adminLoggedIn: Observable<boolean> = this.adminTokenInfo.pipe(mapToLoggedInBoolean);
  private shopOwnerLoggedIn: Observable<boolean> = this.shopOwnerTokenInfo.pipe(mapToLoggedInBoolean);

  constructor(private storage: BrowserStorageService) {
    this.readTokenInfoFromStorage();
    this.attachAutomaticTokenStorage();
  }

  /**
   * Whether the user is currently logged in as admin.
   */
  get isAdmin(): Observable<boolean> {
    return this.adminLoggedIn;
  }

  /**
   * Sets the admin's login state to true.
   *
   * (automatically sets the shop owner's login state to false)
   *
   * @param tokenInfo The token information received from the backend.
   */
  loginAdmin(tokenInfo: TokenInfoDto) {
    this.adminTokenInfo.next(tokenInfo);
    this.shopOwnerTokenInfo.next(LOGGED_OUT);
  }

  /**
   * Sets the admin's login state to false.
   */
  logoutAdmin() {
    this.adminTokenInfo.next(LOGGED_OUT);
  }

  /**
   * Whether the user is currently logged in as shop owner.
   */
  get isShopOwner(): Observable<boolean> {
    return this.shopOwnerLoggedIn;
  }

  /**
   * Sets the shop owner's login state to true.
   *
   * (automatically sets the admin's login state to false)
   *
   * @param tokenInfo The token information received from the backend.
   */
  loginShopOwner(tokenInfo: TokenInfoDto) {
    this.shopOwnerTokenInfo.next(tokenInfo);
    this.adminTokenInfo.next(LOGGED_OUT);
  }

  /**
   * Sets the shop owner's login state to false.
   */
  logoutShopOwner() {
    this.shopOwnerTokenInfo.next(LOGGED_OUT);
  }

  private readTokenInfoFromStorage() {
    const admin = this.storage.get(ADMIN_STORAGE_KEY);
    if (admin) {
      this.adminTokenInfo.next(admin);
    }

    const shopOwner = this.storage.get(SHOP_STORAGE_KEY);
    if (shopOwner) {
      this.shopOwnerTokenInfo.next(shopOwner);
    }
  }

  /**
   * After this method is called, all updates written to the token info subjects will be persisted in browser storage.
   */
  private attachAutomaticTokenStorage() {
    this.adminTokenInfo.subscribe(tokenInfo => {
      this.storage.set(ADMIN_STORAGE_KEY, tokenInfo);
    });

    this.shopOwnerTokenInfo.subscribe(tokenInfo => {
      this.storage.set(SHOP_STORAGE_KEY, tokenInfo);
    });
  }
}
