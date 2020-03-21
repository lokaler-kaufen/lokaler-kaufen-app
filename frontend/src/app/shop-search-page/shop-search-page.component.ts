import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {ContactTypes} from '../shared/contact-types';
import {Observable} from 'rxjs';
import {ShopControllerService, ShopListDto, ShopListEntryDto} from '../data/client';

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

  constructor(private route: ActivatedRoute, private router: Router, private client: HttpClient,
              private shopClient: ShopControllerService) {

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
    const obs: Observable<ShopListDto> = this.shopClient.listNearbyUsingGET(params.location);
    obs.subscribe({
      next: this.dataUpdate,
      error(msg) {
        console.error(msg);
      }
    });
  }

  private dataUpdate(data: ShopListDto): void {
    this.dataSource = new MatTableDataSource<ShopListEntryDto>(data.shops);
  }

}
