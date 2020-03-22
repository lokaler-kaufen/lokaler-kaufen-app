import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {HttpClient} from "@angular/common/http";
import {ShopControllerService} from "../data/client";

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
              private shopClient: ShopControllerService) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  sendConfirmationMail() {
    this.showConfirmDialog = !this.showConfirmDialog;
    this.client.post('/api/shop/send-create-link', {
      email: this.email
    }).subscribe();
  }
}
