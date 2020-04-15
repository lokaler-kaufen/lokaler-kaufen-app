import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';
import {ShopListDto, ShopListEntryDto} from '../data/api';
import {BreakpointObserver} from '@angular/cdk/layout';

@Component({
  selector: 'shop-search-page',
  templateUrl: './shop-search-page.component.html',
  styleUrls: ['./shop-search-page.component.css']
})
export class ShopSearchPageComponent implements OnInit {
  searchBusiness: string;
  // we don't search while typing, so remember the last executed search
  lastSearchString: string;
  location: string;
  newLocation: string;
  shops: ShopListEntryDto[] = [];
  isSmallScreen: boolean;
  isSearchEmpty = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private client: HttpClient,
    private notificationsService: NotificationsService,
    breakpointObserver: BreakpointObserver
  ) {
    // listen to responsive breakpoint changes
    breakpointObserver.observe('(max-width: 719px)').subscribe(
      result => this.isSmallScreen = result.matches
    );
  }

  ngOnInit(): void {
    // listen to changed query params
    this.route.queryParams.subscribe((params) => {
      this.newLocation = params.location;
      if (!this.newLocation) {
        this.router.navigate(['']);
      }

      this.findAllShopsNearby();
    });
  }

  showDetailPage(row: any) {
    this.router.navigate(['/shops/' + row.id]);
  }

  performSearch() {
    if (this.newLocation.length < 5) {
      return;
    }
    if (!this.searchBusiness || this.searchBusiness.trim().length === 0) {
      this.findAllShopsNearby();

    } else {
      this.findShopsBySearchQuery(this.searchBusiness);
    }
  }

  private handleResponse(response) {
    if (response.shops.length > 0) {
      this.shops = [...response.shops]
        .sort((shop1, shop2) => shop1.distance - shop2.distance);
      this.isSearchEmpty = false;

    } else {
      this.shops = [];
      console.log('Keine Läden gefunden.');
      this.isSearchEmpty = true;
    }
    this.lastSearchString = this.searchBusiness;
    this.location = this.newLocation;
    window.history.replaceState({}, '', '/#/shops?location=' + this.location);
  }

  private findAllShopsNearby(): void {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    const params = new HttpParams().append('zipCode', this.newLocation);

    this.client.get<ShopListDto>('/api/shop/nearby', {headers, params}).subscribe(
      response => this.handleResponse(response),
      error => this.handleError(error));
  }

  private findShopsBySearchQuery(query: string): void {
    const params = new HttpParams().append('zipCode', this.newLocation).append('query', query);

    this.client.get<ShopListDto>('/api/shop/search', {params}).subscribe(
      response => this.handleResponse(response),
      error => this.handleError(error));
  }

  private handleError(error) {
    console.log('Error requesting shop overview: ' + error.status + ', ' + error.message);
    if (error.status === 400 && error.error.code === 'LOCATION_NOT_FOUND') {
      this.notificationsService.error('shop.search.error.location-not-found.title', 'shop.search.error.location-not-found.message');
    } else {
      this.notificationsService.error('shop.search.error.generic.title', 'shop.search.error.generic.message');
    }
    this.newLocation = this.location;
    window.history.replaceState({}, '', '/#/shops?location=' + this.location);
  }

  checkZipCodeInput($event) {
    const input = $event.target as HTMLInputElement;

    const originalValue: string = input.value;
    const maxLength = +input.getAttribute('maxlength');

    let value = originalValue.replace(new RegExp(/[^\d]/g), '');
    if (value.length > maxLength) {
      value = value.slice(0, maxLength);
    }

    input.value = value;
  }

  selectAll($event: MouseEvent) {
    const input = $event.target as HTMLInputElement;
    input.select();
  }

  handleKeyEvent($event: KeyboardEvent) {
    if ($event.key === 'Enter') {
      this.performSearch();
    }
  }

}
