import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface BookingDialogData {}

@Component({
  selector: 'booking-popup',
  templateUrl: './booking-popup.component.html',
  styleUrls: ['./booking-popup.component.css']
})
export class BookingPopupComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<BookingPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: BookingDialogData) { }

  ngOnInit(): void {
  }

  onNoClick(): void {
    console.log('onNoClick');
    this.dialogRef.close();
  }

}
