import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ShopDetailDto, ShopListDto, ShopListEntryDto} from '../data/client';
import ContactTypesEnum = ShopDetailDto.ContactTypesEnum;

@Component({
  selector: 'shop-search-page',
  templateUrl: './shop-search-page.component.html',
  styleUrls: ['./shop-search-page.component.css']
})
export class ShopSearchPageComponent implements OnInit {
  contactTypes = Object.keys(ContactTypesEnum).map(key => ContactTypesEnum[key]);
  searchBusiness: string;
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  displayedColumns: string[] = ['name', 'distance', 'supportedContactTypes'];

  constructor(private route: ActivatedRoute, private router: Router, private client: HttpClient) {

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
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    this.client.get<ShopListDto>('/api/shop/nearby?location=' + params.location, {headers: headers}).subscribe(
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
        console.log('Error requesting shop overview: ' + error.status + ', ' + error.error.message);
      }
    );
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

}
