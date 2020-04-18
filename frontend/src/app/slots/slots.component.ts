import {Component, Input, OnInit, Output} from '@angular/core';
import {SlotsPerDay} from '../shop-details-page/shop-details-page.component';
import {Observable, ReplaySubject} from 'rxjs';
import {BreaksDto, SlotsDto} from '../data/client';

/**
 * data structure for slot selection
 */
export interface SlotSelectionData {
  id: string;
  removeSlot: boolean;
}

/**
 * input data structure for slots component
 */
export interface ReserveSlotsData {
  slots: SlotsDto;
  breaks?: BreaksDto;
}

/**
 * Component for handling and displaying slot interactions
 */
@Component({
  selector: 'slots',
  templateUrl: './slots.component.html',
  styleUrls: ['./slots.component.css']
})
export class SlotsComponent implements OnInit {

  /**
   * input data for slots
   */
  @Input()
  slotsDataObservable: Observable<ReserveSlotsData>;

  /**
   * whether it is config mode, needed for different html styles
   */
  @Input()
  isSlotConfig = false;

  slotsPerDay = new Array<SlotsPerDay>();

  /**
   * the selected slot and if it is to remove or to add
   */
  @Output()
  selectedSlotId: ReplaySubject<SlotSelectionData> = new ReplaySubject<SlotSelectionData>();

  weekday = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'];
  today: Date;


  constructor() {
  }

  /**
   * set slots to be displayed based on given input data
   * set availability of the slots if breaks are given
   */
  ngOnInit(): void {
    this.today = new Date();
    this.slotsDataObservable.subscribe(slotsData => {
      this.slotsPerDay = new Array<SlotsPerDay>();
      // convert to map
      Object.keys(slotsData.slots.slots).forEach(key => {
        const availableSlots = slotsData.slots.slots[key];
        if (this.isSlotConfig && slotsData.breaks) {
          availableSlots.forEach(s => {
            if (slotsData.breaks.slotIds.includes(s.id)) {
              s.available = false;
            }
          });
        }
        this.slotsPerDay.push({
          dayName: this.getDayName(key),
          slots: availableSlots,
          hasSlots: slotsData.slots.slots[key].length > 0
        });
      });
    });
  }

  /**
   * emit an event if a slot is selected as break
   * @param slotId the selected slot id
   */
  sendSelectedSlotId(slotId: string) {
    this.slotsPerDay.forEach(day => {
      const filterSlot = day.slots.find(s => s.id === slotId);
      if (filterSlot) {
        filterSlot.available = false;
      }
    });
    this.selectedSlotId.next({
      id: slotId,
      removeSlot: false
    });
  }

  /**
   * emit an event if a slot selected as break is unselected
   * @param slotId the selected slot id
   */
  sendRemovedSlotId(slotId: string) {
    this.slotsPerDay.forEach(day => {
      const filterSlot = day.slots.find(s => s.id === slotId);
      if (filterSlot) {
        filterSlot.available = true;
      }
    });
    this.selectedSlotId.next({
      id: slotId,
      removeSlot: true
    });
  }

  /**
   * Returns the day name of the given slot
   * monday is index 0, add slot offset % 7 to get day name
   */
  getDayName(slotOffset): string {
    if (!this.isSlotConfig) {
      return this.weekday[(this.today.getDay() + parseInt(slotOffset, 10)) % 7];
    }
    return this.weekday[(parseInt(slotOffset, 10) + 1) % 7];
  }
}
