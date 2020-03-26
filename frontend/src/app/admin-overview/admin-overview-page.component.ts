import {Component, OnInit, ViewChild} from '@angular/core';
import {ShopAdminDto, ShopListDto, ShopListEntryDto} from '../data/client';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {Router} from '@angular/router';
import ContactTypesEnum = ShopAdminDto.ContactTypesEnum;

async function requestShops(): Promise<ShopListDto> {
  return fetch('/api/admin/shop')
    .then(response => response.json());
}

@Component({
  selector: 'admin-overview',
  templateUrl: './admin-overview-page.component.html',
  styleUrls: ['./admin-overview-page.component.css']
})
export class AdminOverviewPageComponent implements OnInit {
  displayedColumns = ['id', 'name', 'ownerName', 'street', 'plz', 'city', 'enabled', 'email'];
  dataSource = new MatTableDataSource();

  constructor(private router: Router) {
    this.dataUpdate = this.dataUpdate.bind(this);
    this.dataSource.sort = this.sort;
  }

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  ngOnInit(): void {
    this.generateDummyData();
    requestShops().then(this.dataUpdate);
  }

  private dataUpdate(data: ShopListDto): void {
    this.dataSource = new MatTableDataSource<ShopListEntryDto>(data.shops);
    this.dataSource.sort = this.sort;
  }

  showDetailPage(row: any) {
    this.router.navigate(['/admin/' + row.id]);
  }

  private generateDummyData() {
    function data() {
      return {
        addressSupplement: Math.random().toString(10),
        city: Math.random().toString(10),
        contactTypes: Object.values(ContactTypesEnum).filter(e => Math.random() >= 0.5),
        details: Math.random().toString(10),
        email: Math.random().toString(10),
        enabled: Math.random() * 10 > 5,
        id: Math.random().toString(10).slice(3),
        name: Math.random().toString(10),
        ownerName: Math.random().toString(10),
        street: Math.random().toString(10),
        website: Math.random().toString(10),
        zipCode: Math.random().toString(10)
      } as ShopAdminDto;
    }

    const shopList: Array<ShopAdminDto> = [];
    const n: number = Number.parseInt((Math.random() * 10).toString(), 10) + 1;
    for (let i = 0; i < n; i++) {
      const tempShop = data();
      shopList.push(tempShop);
    }
    this.dataUpdate({shops: shopList} as ShopListDto);
  }

}
