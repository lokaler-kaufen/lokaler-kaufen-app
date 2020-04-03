import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {RegisterBusinessPopupComponent} from '../register-business-popup/register-business-popup.component';
import {UserContextService} from '../shared/user-context.service';
import {HttpClient} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';
import {Router} from '@angular/router';

@Component({
  selector: 'app-footer',
  templateUrl: './app-footer.component.html',
  styleUrls: ['./app-footer.component.css']
})
export class AppFooterComponent implements OnInit {

  // hold the dialog ref as long as the dialog is open
  dialogRef;

  // version info
  versionInfo: VersionInfo;

  constructor(public dialog: MatDialog,
              private client: HttpClient,
              public userContextService: UserContextService,
              private notificationsService: NotificationsService,
              private router: Router) {
  }

  openRegisterBusinessPopup(): void {
    if (!this.dialogRef) {
      this.dialogRef = this.dialog.open(RegisterBusinessPopupComponent, {
        width: '500px'
      });

      this.dialogRef.afterClosed().subscribe(
        () => {
          // Reset the dialogRef because the user should be able to open the dialog again.
          this.dialogRef = null;
        }
      );
    }
  }

  ngOnInit(): void {
    this.client.get<VersionInfo>('/api/info/version').toPromise().then(info => {
      this.versionInfo = info;
    });
  }

  logoutStoreOwner() {
    this.client.delete('/api/shop/login', {}).toPromise().then(() => {
      console.log('Shop Owner logged out. ');
      this.userContextService.storeOwnerLoggedOut();
      this.router.navigate(['/login']);
    }).catch(error => {
      console.log('Unable to logout shop owner: ' + error.status + ' ' + error.message);
      this.notificationsService.error('Tut uns Leid!', 'Beim Logout ist etwas schiefgegangen.');
    });

  }
}

export interface VersionInfo {
  commitHash: string;
  commitTime: string;
  localChanges: string;
  version: string;
}
