import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {ShopAdminDto, ShopsAdminDto} from '../data/client';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {NotificationsService} from 'angular2-notifications';
import {catchError, finalize, map} from 'rxjs/operators';
import {AdminService} from '../shared/admin.service';

export class ShopListAdminDataSource implements DataSource<ShopAdminDto> {

  private shopsSubject = new BehaviorSubject<ShopAdminDto[]>([]);
  private loadingSubject  = new BehaviorSubject<boolean>(false);

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

    this.adminService.listAllShops().pipe(
      map((result: ShopsAdminDto) => result.shops),
      catchError((error) => {
        console.log('Got error on request to /api/admin/shop' + error);
        this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Daten aufgetreten.');
        return of([]);
      }),
      finalize(() => this.loadingSubject.next(false))
    ).subscribe((shops: ShopAdminDto[]) => {
      this.shopsSubject.next(shops);
    });

  }

}
