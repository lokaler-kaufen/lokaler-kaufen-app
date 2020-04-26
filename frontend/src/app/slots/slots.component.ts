import {Component, Input, OnInit, Output} from '@angular/core';
import {Observable, ReplaySubject} from 'rxjs';
import {BreakDto, BreaksDto, SlotDto, SlotsDto} from '../data/api';

/**
 * data structure for slot selection
 */
export interface SlotSelectionData {
  slot: SlotDto;
  index: number;
  day: string;
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
 * Data structure to represent slots per day.
 */
interface Day {
  dayName: string;
  day: string;
  hasSlots: boolean;
  isHoliday: boolean;
  slots: Array<SlotDto>;
}

const DAY_NAMES = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'];

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
   * Slot data to be displayed.
   */
  @Input()
  data: Observable<ReserveSlotsData>;

  /**
   * Whether the "config" mode is active or not.
   */
  @Input()
  isSlotConfig = false;

  /**
   * the selected slot and if it is to remove or to add
   */
  @Output()
  selectedSlot: ReplaySubject<SlotSelectionData> = new ReplaySubject<SlotSelectionData>();

  days: Array<Day> = [];
  today: Date;

  /**
   * set slots to be displayed based on given input data
   * set availability of the slots if breaks are given
   */
  ngOnInit(): void {
    this.today = new Date();

    this.data.subscribe(slotsData => {
      this.days = [];

      if (slotsData.breaks) {
        this.setAvailableSlots(slotsData);

      } else {
        slotsData.slots.days.forEach(day => {
          this.days.push({
            dayName: this.getDayName(new Date(day.date)),
            day: day.dayOfWeek.toLowerCase(),
            slots: day.slots,
            hasSlots: day.slots.length > 0,
            isHoliday: day.holiday
          });
        });
      }
    });
  }

  /**
   * Set availability of break slots and emit events with preselected break slots
   * @param slotsData the data containing slots and breaks
   */
  private setAvailableSlots(slotsData: ReserveSlotsData) {
    Object.keys(slotsData.breaks).forEach(key => {
      const availableSlots = slotsData.slots.days.find(day => day.dayOfWeek === key.toUpperCase());
      if (this.isSlotConfig && availableSlots) {
        availableSlots.slots.forEach((s, i) => {
          if (this.isBreakSlot(s, slotsData.breaks[key])) {
            s.available = false;
            this.sendSelectedSlotId(s, i, key);
          }
        });
      }
      this.days.push({
        dayName: this.getDayName(new Date(availableSlots.date)),
        day: key,
        slots: availableSlots.slots,
        hasSlots: availableSlots.slots.length > 0,
        isHoliday: availableSlots.holiday
      });
    });
  }

  /**
   * Checks if the given slot is in the break times
   * @param slot the slot
   * @param breaksData the breaks
   */
  private isBreakSlot(slot: SlotDto, breaksData: BreakDto[]): boolean {
    let isBreak = false;
    breaksData.forEach(breakData => {
      // slot in break time
      if (slot.start >= breakData.start && slot.end <= breakData.end ||
        // slot start in break time
        slot.start < breakData.end && slot.end >= breakData.end ||
        // slot end in break time
        slot.end > breakData.start && slot.start <= breakData.start) {
        isBreak = true;
      }
    });
    return isBreak;
  }

  /**
   * Emits the next "selected" slot.
   *
   * @param slot The selected slot's id.
   * @param index The index of the selected slot, needed to check subsequent slots.
   * @param day The day to which the selected slot belongs to.
   */
  sendSelectedSlotId(slot: SlotDto, index: number, day: string) {
    this.selectedSlot.next({
      slot,
      index,
      day,
      removeSlot: false
    });

    // In non-config mode, the UI should not eagerly change the displayed state of the slot.
    // see https://github.com/lokaler-kaufen/lokaler-kaufen-app/issues/252
    if (this.isSlotConfig) {
      slot.available = false;
    }
    /*
     ACTUALLY, this whole interaction between the parent and the <slot> component is rather weird.

     Ideally, the parent should "own" and provide the data that is simply displayed inside the <slot> component.
     The event that's raised in the slot component whenever the user clicks on a slot button should not mutate the
     <slot> component's state. The parent should rather update the data that's flowing into the <slot> component.
    */
  }

  /**
   * Emits the next "to be removed" slot.
   *
   * @param slot The selected slot's id.
   * @param index The index of the selected slot, needed to check subsequent slots.
   * @param day The day to which the selected slot belongs to.
   */
  sendRemovedSlotId(slot: SlotDto, index: number, day: string) {
    this.selectedSlot.next({
      slot,
      index,
      day,
      removeSlot: true
    });

    // In non-config mode, the UI should not eagerly change the displayed state of the slot.
    // see https://github.com/lokaler-kaufen/lokaler-kaufen-app/issues/252
    if (this.isSlotConfig) {
      slot.available = true;
    }
  }

  /**
   * Returns the human-readable day name for the given date.
   */
  getDayName(date: Date): string {
    return DAY_NAMES[date.getDay()];
  }

}
