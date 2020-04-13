import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

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
export class BookingSuccessPopupComponent {

  constructor(public dialogRef: MatDialogRef<BookingSuccessPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: BookingSuccessData) {
  }

  /**
   * Basically copies the "data" object but properly serializes the day.
   */
  get uiData(): BookingSuccessData {
    return {
      ...this.data,
      day: new Date(this.data.day).toLocaleDateString('de-DE')
    };
  }

  onClick(): void {
    this.dialogRef.close();
  }

}
