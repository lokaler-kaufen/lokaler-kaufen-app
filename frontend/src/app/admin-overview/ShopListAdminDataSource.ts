import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable} from 'rxjs';
import {NotificationsService} from 'angular2-notifications';
import {AdminService} from '../service/admin.service';
import {ShopAdminDto} from '../data/api';

export class ShopListAdminDataSource implements DataSource<ShopAdminDto> {

  private shopsSubject = new BehaviorSubject<ShopAdminDto[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  constructor(private adminService: AdminService, private notificationsService: NotificationsService) {
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

    this.adminService.listAllShops()
      // unwrap the shops from the response on success
      .then(({shops}) => shops)

      // handle error
      .catch(error => {
        console.log('Got error on request to /api/admin/shop' + error);
        this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Daten aufgetreten.');
        // this means the next "then" will receive this as fallback value because no value could be retrieved above
        return [];
      })

      // update subjects
      .then(shops => {
        this.shopsSubject.next(shops);
        this.loadingSubject.next(false);
      });
  }

}
