import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {FormControl, Validators} from '@angular/forms';
import {AsyncNotificationService} from '../i18n/async-notification.service';

@Component({
  selector: 'register-business-popup',
  templateUrl: './register-business-popup.component.html',
  styleUrls: ['./register-business-popup.component.css']
})
export class RegisterBusinessPopupComponent {
  showConfirmDialog = false;

  email: FormControl = new FormControl('', [Validators.required,
    Validators.pattern('^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')]);

  constructor(private client: HttpClient,
              public dialogRef: MatDialogRef<RegisterBusinessPopupComponent>,
              private notificationsService: AsyncNotificationService) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  sendConfirmationMail() {
    if (!this.email.valid) {
      return;
    }
    this.client.post('/api/shop/send-create-link', {
      email: this.email.value
    }).subscribe(() => {
        console.log('Shop creation link sent. ');
        this.showConfirmDialog = !this.showConfirmDialog;
      },
      error => {
        console.log('Error sending creation link: ' + error.status + ', ' + error.message + ', ' + error.headers.get('x-trace-id'));
        if (error.status === 409 && error.error.code === 'SHOP_ALREADY_EXISTS') {
          this.notificationsService.error(
            'registration.popup.error.already-registered.title',
            'registration.popup.error.already-registered.message'
          );

        } else {
          this.notificationsService.error(
            'registration.popup.error.generic.title',
            'registration.popup.error.generic.message');
        }
      });
  }
}
