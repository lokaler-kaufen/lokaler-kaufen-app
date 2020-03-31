import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';
import {FormControl, Validators} from '@angular/forms';

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
              private notificationsService: NotificationsService) {
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
        console.log('Error sending creation link: ' + error.status + ', ' + error.message);
        if (error.status === '409' && error.error.code === 'SHOP_ALREADY_EXISTS') {
          this.notificationsService.error(
            'Moment mal...',
            'Sie haben sich bereits registriert. Sie können sich unter Login für Ladenbesitzer anmelden.'
          );

        } else {
          this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Senden Ihres Links aufgetreten.');
        }
      });
  }
}
