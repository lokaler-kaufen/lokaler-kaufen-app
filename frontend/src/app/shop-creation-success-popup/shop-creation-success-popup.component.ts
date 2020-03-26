import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'shop-creation-success-popup',
  templateUrl: './shop-creation-success-popup.component.html',
  styleUrls: ['./shop-creation-success-popup.component.css']
})
export class ShopCreationSuccessPopupComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ShopCreationSuccessPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: string) {
  }

  ngOnInit(): void {
  }

}
