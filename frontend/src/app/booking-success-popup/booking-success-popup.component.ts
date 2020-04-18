import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ContactTypesEnum} from '../contact-types/available-contact-types';

export class BookingSuccessData {
  owner: string;
  contactNumber: string;
  contactType: string;
  start: string;
  end: string;
  day: string;
}

@Component({
  selector: 'booking-popup',
  templateUrl: './booking-success-popup.component.html',
  styleUrls: ['./booking-success-popup.component.css']
})
export class BookingSuccessPopupComponent implements OnInit {
  contactTypes = ContactTypesEnum;

  constructor(public dialogRef: MatDialogRef<BookingSuccessPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: BookingSuccessData) {
  }

  ngOnInit(): void {
  }

  getDate() {
    const date = new Date(this.data.day);
    return date.toLocaleDateString('de-DE');
  }

  onClick(): void {
    this.dialogRef.close();
  }

}
