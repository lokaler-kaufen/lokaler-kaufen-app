import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';
import {ShopListDto} from '../data/client/model/shopListDto';
import {ShopListEntryDto} from '../data/client/model/shopListEntryDto';

@Component({
  selector: 'shop-search-page',
  templateUrl: './shop-search-page.component.html',
  styleUrls: ['./shop-search-page.component.css']
})
export class ShopSearchPageComponent implements OnInit {
  searchBusiness: string;
  location: string;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['name', 'distance', 'supportedContactTypes'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private client: HttpClient,
    private notificationsService: NotificationsService
  ) {
  }

  ngOnInit(): void {
    // prepare default sort
    this.sort.sort({id: 'distance', start: 'asc', disableClear: false});

    // listen to changed query params
    this.route.queryParams.subscribe((params) => {
      this.location = params.location;
      if (!this.location) {
        this.router.navigate(['']);
      }

      this.findAllShopsNearby();
    });
  }

  showDetailPage(row: any) {
    this.router.navigate(['/shops/' + row.id]);
  }

  performSearch() {
    if (!this.searchBusiness || this.searchBusiness.trim().length === 0) {
      this.findAllShopsNearby();

    } else {
      this.findShopsBySearchQuery(this.searchBusiness);
    }
  }

  private handleResponse(response) {
    if (response.shops.length > 0) {
      this.dataSource = new MatTableDataSource<ShopListEntryDto>(response.shops);

    } else {
      this.dataSource = new MatTableDataSource<ShopListEntryDto>([]);
      console.log('Keine Shops gefunden.');
    }

    this.dataSource.sort = this.sort;
  }

  private findAllShopsNearby(): void {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    const params = new HttpParams().append('zipCode', this.location);

    this.client.get<ShopListDto>('/api/shop/nearby', {headers, params}).subscribe(
      response => this.handleResponse(response),
      error => {
        console.log('Error requesting shop overview: ' + error.status + ', ' + error.message);
        this.notificationsService.error('Tut uns leid!', 'Ein Fehler beim Laden der Shops ist aufgetreten.');
      }
    );
  }

  private findShopsBySearchQuery(query: string): void {
    const params = new HttpParams().append('zipCode', this.location).append('query', query);

    this.client.get<ShopListDto>('/api/shop/search', {params}).subscribe(
      response => this.handleResponse(response),
      error => {
        console.log('Error requesting shop overview: ' + error.status + ', ' + error.message);
        this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler bei der Suche aufgetreten.');
      }
    );
  }

  clearSearchOnEmptyInput($event) {
    if (!$event.target.value) {
      this.findAllShopsNearby();
    }
  }
}
