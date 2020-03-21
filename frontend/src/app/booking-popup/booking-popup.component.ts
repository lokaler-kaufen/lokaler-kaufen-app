import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ContactTypes} from '../shared/contact-types';

export interface BookingDialogData {
  supportedContactTypes: string[];
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
  contactTypes = ContactTypes;
  formControl = new FormGroup({
    option: new FormControl('', [Validators.required]),
    phoneNumber: new FormControl('', [Validators.required]),
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [
      Validators.required,
      Validators.email]
    )
  });

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
