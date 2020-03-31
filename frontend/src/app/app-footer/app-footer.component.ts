import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {RegisterBusinessPopupComponent} from '../register-business-popup/register-business-popup.component';
import {HttpClient} from '@angular/common/http';

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

  constructor(public dialog: MatDialog, private client: HttpClient) {
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

}

export interface VersionInfo {
  commitHash: string;
  commitTime: string;
  localChanges: string;
  version: string;
}
