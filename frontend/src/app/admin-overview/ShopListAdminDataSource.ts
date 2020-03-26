import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {ShopAdminDto, ShopsAdminDto} from '../data/client';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';

export class ShopListAdminDataSource implements DataSource<ShopAdminDto> {

  private shopsSubject = new BehaviorSubject<ShopAdminDto[]>([]);
  private loadingSubject  = new BehaviorSubject<boolean>(false);
  private errorSubject = new BehaviorSubject<void>(null);

  public loading$ = this.loadingSubject.asObservable();
  public error$ = this.errorSubject.asObservable();

  constructor(private httpClient: HttpClient, private notificationsService: NotificationsService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<ShopAdminDto[] | ReadonlyArray<ShopAdminDto>> {
    return this.shopsSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.shopsSubject.complete();
    this.loadingSubject.complete();
  }

  // we could introduce paging here?
  loadShops() {
    this.loadingSubject.next(true);

    // todo can we reactify this?!?!
    // todo use pipe at least once!
    this.httpClient.get<ShopsAdminDto>('/api/admin/shop').subscribe(
      (shops: ShopsAdminDto) => {
        this.shopsSubject.next(shops.shops);
        this.loadingSubject.next(false);
      }, (error: any) => {
        console.log('Got error on request to /api/admin/shop' + error);
        this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Daten aufgetreten.');
        this.loadingSubject.next(false);
        this.errorSubject.next();
      });
  }

}
