import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ShopListAdminDataSource} from './ShopListAdminDataSource';
import {NotificationsService} from 'angular2-notifications';
import {AdminService} from '../service/admin.service';

@Component({
  selector: 'admin-overview',
  templateUrl: './admin-overview-page.component.html',
  styleUrls: ['./admin-overview-page.component.css']
})
export class AdminOverviewPageComponent implements OnInit {
  public displayedColumns = ['name', 'ownerName', 'street', 'zipCode', 'city', 'email', 'approved'];
  public dataSource: ShopListAdminDataSource;

  constructor(private adminService: AdminService,
              private notificationsService: NotificationsService,
              private router: Router) {
    this.dataSource = new ShopListAdminDataSource(adminService, notificationsService);
  }

  ngOnInit(): void {
    this.dataSource.loadShops();
  }

  showDetailPage(row: any) {
    this.router.navigate(['/admin/', row.id]);
  }
}
