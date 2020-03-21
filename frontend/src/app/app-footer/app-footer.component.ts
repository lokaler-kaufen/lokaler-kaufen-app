import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {RegisterBusinessPopupComponent} from "../register-business-popup/register-business-popup.component";

@Component({
  selector: 'app-footer',
  templateUrl: './app-footer.component.html',
  styleUrls: ['./app-footer.component.css']
})
export class AppFooterComponent implements OnInit {


  constructor(public dialog: MatDialog) {
  }

  openRegisterBusinessPopup(): void {
    const dialogRef = this.dialog.open(RegisterBusinessPopupComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe();
  }

  ngOnInit(): void {
  }

}
