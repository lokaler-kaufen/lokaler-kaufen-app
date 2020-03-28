import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';
import {ShopListDto} from '../data/client/model/shopListDto';
import {ShopListEntryDto} from '../data/client/model/shopListEntryDto';
import {ShopDetailDto} from '../data/client/model/shopDetailDto';
import ContactTypesEnum = ShopDetailDto.ContactTypesEnum;

@Component({
  selector: 'shop-search-page',
  templateUrl: './shop-search-page.component.html',
  styleUrls: ['./shop-search-page.component.css']
})
export class ShopSearchPageComponent implements OnInit {
  contactTypes = Object.keys(ContactTypesEnum).map(key => ContactTypesEnum[key]);
  searchBusiness: string;
  location: string;
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  displayedColumns: string[] = ['name', 'distance', 'supportedContactTypes'];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private client: HttpClient,
              private notificationsService: NotificationsService) {

    this.dataUpdate = this.dataUpdate.bind(this);
    this.handleParamsUpdate = this.handleParamsUpdate.bind(this);
  }

  ngOnInit(): void {
    // listen to changed query params
    this.route.queryParams.subscribe(this.handleParamsUpdate);
  }

  showDetailPage(row: any) {
    this.router.navigate(['/shops/' + row.id]);
  }

  private handleParamsUpdate(params): void {
    this.location = params.location;
    if (!this.location) {
      this.router.navigate(['']);
    }
    this.findAllShopsNearby();
  }

  private dataUpdate(data: ShopListDto): void {
    this.dataSource = new MatTableDataSource<ShopListEntryDto>(data.shops);
  }

  getEnumValue(contactType: any) {
    let splitted = contactType.split('_');
    splitted = splitted.map(split => {
      return split.charAt(0) + split.slice(1).toLowerCase();
    });
    return splitted.join(' ');
  }

  performSearch() {
    if (!this.searchBusiness || this.searchBusiness.trim().length === 0) {
      this.findAllShopsNearby();
    } else {
      this.findShopsBySearchQuery(this.searchBusiness);
    }
  }

  private findAllShopsNearby(): void {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    const params = new HttpParams().append('zipCode', this.location);

    this.client.get<ShopListDto>('/api/shop/nearby', {headers, params}).subscribe(
      response => {
        if (response.shops.length > 0) {
          this.dataSource = new MatTableDataSource<ShopListEntryDto>(response.shops);
          this.sort.sort({id: 'distance', start: 'asc', disableClear: false});
          this.dataSource.sort = this.sort;
        } else {
          console.log('Keine Shops gefunden.');
        }
      },
      error => {
        console.log('Error requesting shop overview: ' + error.status + ', ' + error.message);
        this.notificationsService.error('Tut uns leid!', 'Ein Fehler beim Laden der Shops ist aufgetreten.');
      }
    );
  }

  private findShopsBySearchQuery(query: string): void {
    const params = new HttpParams().append('zipCode', this.location).append('query', query);

    this.client.get<ShopListDto>('/api/shop/search', {params}).subscribe(
      response => {
        this.dataSource = new MatTableDataSource<ShopListEntryDto>();
        if (response.shops.length > 0) {
          this.dataSource = new MatTableDataSource<ShopListEntryDto>(response.shops);
          this.sort.sort({id: 'distance', start: 'asc', disableClear: false});
          this.dataSource.sort = this.sort;
        } else {
          console.log('Keine Shops gefunden.');
        }
      },
      error => {
        console.log('Error requesting shop overview: ' + error.status + ', ' + error.message);
        this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler bei der Suche aufgetreten.');
      }
    );
  }

}
