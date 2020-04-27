import {Component} from '@angular/core';
import {AdminService} from '../service/admin.service';

@Component({
  selector: 'admin-login',
  templateUrl: './admin-login-page.component.html',
  styleUrls: ['./admin-login-page.component.css']
})
export class AdminLoginPageComponent {

  constructor(private adminService: AdminService) {
  }

  onSuccessfulLogin() {
    this.adminService.onSuccessfulLogin();
  }

}
