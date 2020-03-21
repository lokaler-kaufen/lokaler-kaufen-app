import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {ShopOverviewDto} from "./shop-overview-dto";
import {MatSort} from "@angular/material/sort";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ContactTypes} from "../shared/contact-types";

@Component({
  selector: 'shop-search-page',
  templateUrl: './shop-search-page.component.html',
  styleUrls: ['./shop-search-page.component.css']
})
export class ShopSearchPageComponent implements OnInit {
  contactTypes = ContactTypes;
  searchBusiness: string;
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  displayedColumns: string[] = ['name', 'distance', 'supportedContactTypes'];

  // MOCK STUFF
  shopOverviews = [new ShopOverviewDto('abc-123', 'Moes Whiskyladen', 5.7, ['facetime', 'whatsapp']),
    new ShopOverviewDto('def-123', 'Flos Kaffeeladen', 0.7, ['phone', 'facetime']),
    new ShopOverviewDto('ghi-123', 'Vronis Kleiderladen', 0.0, ['phone', 'facetime', 'whatsapp'])];

  constructor(private route: ActivatedRoute, private router: Router, private client: HttpClient) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      console.log('Backend call for location: ' + params.location);
      const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
      this.client.get('/api/shop/find?location=' + params.location, {headers: headers}).subscribe(
        response => {
          if (response['shops'].length > 0) {
            this.dataSource = new MatTableDataSource<ShopOverviewDto>(response['shops']);
            this.sort.sort({id: 'distance', start: 'asc', disableClear: false});
            this.dataSource.sort = this.sort;
          } else {
            console.log('Keine Shops gefunden.');
          }
        },
        error => {
          console.log('Error requesting shop overview: ' + error.status + ', ' + error.error.message);
          this.dataSource = new MatTableDataSource<ShopOverviewDto>(this.shopOverviews);
          this.sort.sort({id: 'distance', start: 'asc', disableClear: false});
          this.dataSource.sort = this.sort;
        }
      );
    });
  }

  showDetailPage(row: any) {
    console.log('Row ID: ' + row.id);
    this.router.navigate(['/shops/' + row.id]);
  }

}
