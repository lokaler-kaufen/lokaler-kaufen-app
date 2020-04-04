import {Component} from '@angular/core';
import {UserContextService} from '../../shared/user-context.service';
import {HttpClient} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';
import {Router} from '@angular/router';

@Component({
  selector: 'normal-layout',
  templateUrl: './normal-layout.component.html',
  styleUrls: ['./normal-layout.component.css']
})
export class NormalLayoutComponent {

  constructor(
    private client: HttpClient,
    private userContextService: UserContextService,
    private router: Router,
    private notificationsService: NotificationsService
  ) {
  }

  get isLoggedInShopOwner(): boolean {
    return this.userContextService.isLoggedInStoreOwner;
  }

  logout() {
    this.client.delete('/api/shop/login', {}).toPromise()
      .then(() => {
        console.log('Shop Owner logged out. ');
        this.userContextService.storeOwnerLoggedOut();
        this.router.navigate(['']);
      })
      .catch(error => {
        console.log('Unable to logout shop owner: ' + error.status + ' ' + error.message);
        this.notificationsService.error('Tut uns Leid!', 'Beim Logout ist etwas schiefgegangen.');
      });
  }

}
