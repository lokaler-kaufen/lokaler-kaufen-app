import {Component, OnInit} from '@angular/core';
import {AdminService} from '../../shared/admin.service';
import {Router} from '@angular/router';

@Component({
  selector: 'admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent implements OnInit {

  isLoggedInAdmin = false;

  constructor(private adminService: AdminService, private router: Router) {
  }

  ngOnInit(): void {
    this.adminService.getAdminLoginState()
      .subscribe(adminLoggedIn => this.isLoggedInAdmin = adminLoggedIn);
  }

  logout() {
    this.adminService.logout()
      .finally(() => this.router.navigate(['']));
  }

}
