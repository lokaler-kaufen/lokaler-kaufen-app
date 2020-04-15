import {Component, Input, OnInit, Output} from '@angular/core';
import {SlotsPerDay} from '../shop-details-page/shop-details-page.component';
import {ReplaySubject} from 'rxjs';

@Component({
  selector: 'slots',
  templateUrl: './slots.component.html',
  styleUrls: ['./slots.component.css']
})
export class SlotsComponent implements OnInit {

  @Input()
  slotsPerDay: Array<SlotsPerDay> = [];

  @Output()
  selectedSlotId: ReplaySubject<string> = new ReplaySubject<string>();

  constructor() {
  }

  ngOnInit(): void {
  }

  sendSelectedSlotId(slotId: string) {
    this.selectedSlotId.next(slotId);
  }

}
