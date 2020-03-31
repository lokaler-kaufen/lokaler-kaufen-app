import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ContactTypesEnum} from '../contact-types/available-contact-types';

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
  contactTypes = ContactTypesEnum;
  bookingGroup: FormGroup;

  constructor(public dialogRef: MatDialogRef<BookingPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: BookingDialogData) {
  }

  ngOnInit(): void {
    this.bookingGroup = new FormGroup({
      option: new FormControl('', [Validators.required]),
      phoneNumber: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required]),
      email: new FormControl('', [
        Validators.required,
        Validators.pattern('^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')]
      )
    });
    this.bookingGroup.controls.option.setValue(this.contactTypes.getDisplayName(this.data.supportedContactTypes[0]));
  }

  onSubmit(): void {
    if (!this.bookingGroup.valid) {
      return;
    }
    const formValue: any = this.bookingGroup.value;
    const popupResult: BookingPopupResult = {
      outcome: BookingPopupOutcome.BOOK,
      phoneNumber: formValue.phoneNumber,
      name: formValue.name,
      email: formValue.email,
      option: this.contactTypes.getEnumValue([formValue.option])
    };
    this.dialogRef.close(popupResult);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
