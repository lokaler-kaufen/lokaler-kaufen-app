import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormControl, FormGroup} from '@angular/forms';

export interface BookingDialogData {
}

export interface BookingPopupResult {
  outcome: BookingPopupOutcome;
  option: string;
  phoneNumber: string;
  name: string;
  email: string;
}

export enum BookingPopupOutcome {
  BOOK, CANCEL
}

@Component({
  selector: 'booking-popup',
  templateUrl: './booking-popup.component.html',
  styleUrls: ['./booking-popup.component.css']
})
export class BookingPopupComponent implements OnInit {

  formControl = new FormGroup({
    option: new FormControl(''),
    phoneNumber: new FormControl(''),
    name: new FormControl(''),
    email: new FormControl('')
  });

  options: string[] = [
    "WhatsApp",
    "Facetime"
  ];

  constructor(public dialogRef: MatDialogRef<BookingPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: BookingDialogData) {
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const formValue: any = this.formControl.value;

    const popupResult: BookingPopupResult = {
      outcome: BookingPopupOutcome.BOOK,
      phoneNumber: formValue.phoneNumber,
      name: formValue.name,
      email: formValue.email,
      option: formValue.option
    };

    this.dialogRef.close(popupResult);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
