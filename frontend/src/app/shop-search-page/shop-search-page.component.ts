import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {ShopOverviewDto} from "./shop-overview-dto";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'shop-search-page',
  templateUrl: './shop-search-page.component.html',
  styleUrls: ['./shop-search-page.component.css']
})
export class ShopSearchPageComponent implements OnInit {
  searchBusiness: string;
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  displayedColumns: string[] = ['name', 'distance', 'supportedContactTypes'];
  possibleContactTypes = ['Phone', 'Voice Chat', 'WhatsApp'];

  // MOCK STUFF
  shopOverviews = [new ShopOverviewDto('abc-123', 'Moes Whiskyladen', 5.7, ['Phone', 'Voice Chat']),
    new ShopOverviewDto('def-123', 'Flos Kaffeeladen', 0.7, ['Voice Chat', 'WhatsApp']),
    new ShopOverviewDto('ghi-123', 'Vronis Kleiderladen', 0.0, ['Phone', 'Voice Chat', 'WhatsApp'])];

  constructor() {
    this.dataSource = new MatTableDataSource<ShopOverviewDto>(this.shopOverviews);
  }

  ngOnInit(): void {
    console.log('shopOverviews: ' + this.shopOverviews[0].name);
    this.sort.sort({ id: 'distance', start: 'asc', disableClear: false });
    this.dataSource.sort = this.sort;
  }

  showDetailPage(row: any) {
    console.log('Click on: ' + row.name);
    console.log('Includes: ' + row.supportedContactTypes);
  }
}
