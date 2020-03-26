import {Component, OnInit} from '@angular/core';
import {ShopAdminControllerService, ShopAdminDto} from '../data/client';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {ShopListAdminDataSource} from './ShopListAdminDataSource';
import {NotificationsService} from 'angular2-notifications';

@Component({
  selector: 'admin-overview',
  templateUrl: './admin-overview-page.component.html',
  styleUrls: ['./admin-overview-page.component.css']
})
export class AdminOverviewPageComponent implements OnInit {
  public displayedColumns = ['name', 'ownerName', 'street', 'plz', 'city', 'email', 'enabled'];
  public dataSource: ShopListAdminDataSource;

  constructor(private adminControllerService: ShopAdminControllerService,
              private httpClient: HttpClient,
              private notificationsService: NotificationsService,
              private router: Router) {
    this.dataSource = new ShopListAdminDataSource(httpClient, notificationsService);
  }

  ngOnInit(): void {
    this.dataSource.loadShops();
  }

  showDetailPage(row: any) {
    this.router.navigate(['/admin/', row.id]);
  }
}
