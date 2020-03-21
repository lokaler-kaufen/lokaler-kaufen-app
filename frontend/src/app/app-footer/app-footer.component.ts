import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {RegisterBusinessPopupComponent} from "../register-business-popup/register-business-popup.component";

@Component({
  selector: 'app-footer',
  templateUrl: './app-footer.component.html',
  styleUrls: ['./app-footer.component.css']
})
export class AppFooterComponent implements OnInit {

  // hold the dialog ref as long as the dialog is open
  dialogRef;

  constructor(public dialog: MatDialog) {
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
  }

}
