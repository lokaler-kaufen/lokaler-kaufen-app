import {Component} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {PasswordResetPopupComponent} from '../password-reset-popup/password-reset-popup.component';
import {HttpClient} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';
import {ShopOwnerService} from '../shared/shop-owner.service';
import {BreakpointObserver} from '@angular/cdk/layout';
import {RegisterBusinessPopupComponent} from '../register-business-popup/register-business-popup.component';

@Component({
  selector: 'login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {

  private dialogRef;

  isSmallScreen: boolean;

  constructor(
    private matDialog: MatDialog,
    private client: HttpClient,
    private notificationsService: NotificationsService,
    private shopOwnerService: ShopOwnerService,
    breakpointObserver: BreakpointObserver
  ) {
    // listen to responsive breakpoint changes
    breakpointObserver.observe('(max-width: 719px)').subscribe(
      result => this.isSmallScreen = result.matches
    );
  }

  passwordReset(): void {
    this.matDialog.open(PasswordResetPopupComponent, {width: '500px'})
      .afterClosed().subscribe(result => {
      if (result) {
        this.client.post('/api/shop/send-password-reset-link', result).subscribe(() => {
            this.notificationsService.success(
              'Alles klar!',
              'Wir haben Ihnen eine E-Mail zum Zurücksetzen Ihres Passworts geschickt. Sollte die Mail nicht ' +
              'angekommen sein, schauen Sie bitte auch in Ihren Spam-Ordner.'
            );
          },
          error => {
            console.log('Error password reset: ' + error.status + ', ' + error.message);
            this.notificationsService.error(
              'Tut uns leid!',
              'Es ist ein Fehler beim Zurücksetzen Ihres Passworts aufgetreten.'
            );
          });
      }
    });
  }

  signup(): void {
    this.matDialog.open(RegisterBusinessPopupComponent, {width: '500px'});
  }

  onLoginSuccess(): void {
    this.shopOwnerService.storeOwnerLoggedIn();
  }

}
