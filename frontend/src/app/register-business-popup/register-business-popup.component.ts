import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {HttpClient} from "@angular/common/http";
import {NotificationsService} from "angular2-notifications";

@Component({
  selector: 'register-business-popup',
  templateUrl: './register-business-popup.component.html',
  styleUrls: ['./register-business-popup.component.css']
})
export class RegisterBusinessPopupComponent {
  email: string;
  showConfirmDialog = false;

  constructor(private client: HttpClient,
              public dialogRef: MatDialogRef<RegisterBusinessPopupComponent>,
              private notificationsService: NotificationsService) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  sendConfirmationMail() {
    this.showConfirmDialog = !this.showConfirmDialog;
    this.client.post('/api/shop/send-create-link', {
      email: this.email
    }).subscribe(() => console.log('Shop creation link sent. '),
      error => {
        console.log('Error sending creation link: ' + error.status + ', ' + error.error.message);
        this.notificationsService.error('Tut uns leid!', 'Ein Fehler beim Laden der Shops ist aufgetreten.');
      });
  }
}
