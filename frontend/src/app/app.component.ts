import {Component, OnInit} from '@angular/core';
import {MatIconRegistry} from '@angular/material/icon';
import {DomSanitizer} from '@angular/platform-browser';
import {AdminService} from './service/admin.service';
import {HttpClient} from '@angular/common/http';
import {ShopOwnerService} from './shared/shop-owner.service';
import {Router} from '@angular/router';
import {NotificationsService} from 'angular2-notifications';

const additionalIcons: Array<{ id: string, asset: string; }> = [
  {id: 'WHATSAPP', asset: '../assets/whatsapp.svg'},
  {id: 'FACETIME', asset: '../assets/voice_chat.svg'},
  {id: 'PHONE', asset: '../assets/phone.svg'},
  {id: 'GOOGLE_DUO', asset: '../assets/google_duo.svg'},
  {id: 'SKYPE', asset: '../assets/skype.svg'},
  {id: 'VIBER', asset: '../assets/viber.svg'},
  {id: 'SIGNAL', asset: '../assets/signal.svg'}
];

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  isIEOrEdge = /msie\s|trident\/|edge\//i.test(window.navigator.userAgent);

  isAdmin = false;
  isShopOwner = false;

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private adminService: AdminService,
    private client: HttpClient,
    private shopOwnerService: ShopOwnerService,
    private router: Router,
    private notificationsService: NotificationsService
  ) {
    additionalIcons.forEach(({asset, id}) => {
      this.matIconRegistry.addSvgIcon(id, this.domSanitizer.bypassSecurityTrustResourceUrl(asset));
    });
  }

  ngOnInit(): void {
    this.adminService.getAdminLoginState().subscribe(l => this.isAdmin = l);
    this.shopOwnerService.shopOwnerLoggedIn.subscribe(l => this.isShopOwner = l);
  }

  manageShop() {
    this.router.navigate(['/manage-shop']);
  }

  logoutShopOwner() {
    this.shopOwnerService.logout()
      .catch(() => this.notificationsService.error('Tut uns Leid!', 'Beim Logout ist etwas schiefgegangen.'))
      .finally(() => this.router.navigate(['']));
  }

  goToAdminOverview() {
    this.router.navigate(['/admin/overview']);
  }

  logoutAdmin() {
    this.adminService.logout()
      .finally(() => this.router.navigate(['']));
  }

}
