import {Component, Input, OnInit, Output} from '@angular/core';
import {Observable, ReplaySubject} from 'rxjs';
import {BreakDto, BreaksDto, SlotDto, SlotsDto} from '../data/client';

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
 * data structure to represent slots in html
 */
export interface SlotsPerDay {
  dayName: string;
  day: string;
  hasSlots: boolean;
  isHoliday: boolean;
  slots: Array<SlotDto>;
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
  selectedSlot: ReplaySubject<SlotSelectionData> = new ReplaySubject<SlotSelectionData>();

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
      if (slotsData.breaks) {
        this.setAvailableSlots(slotsData);
      } else {
        slotsData.slots.days.forEach(day => {
          this.slotsPerDay.push({
            dayName: this.getDayName(new Date(day.date)),
            day: day.dayOfWeek,
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
      this.slotsPerDay.push({
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
        slot.start <= breakData.end && slot.end >= breakData.end ||
        // slot end in break time
        slot.end >= breakData.start && slot.start <= breakData.start) {
        isBreak = true;
      }
    });
    return isBreak;
  }

  /**
   * emit an event if a slot is selected as break
   * @param slot the selected slot id
   * @param index the index of the selected slot, needed to check subsequent slots
   */
  sendSelectedSlotId(slot: SlotDto, index: number, day: string) {
    this.selectedSlot.next({
      slot,
      index,
      day,
      removeSlot: false
    });
    slot.available = false;
  }

  /**
   * emit an event if a slot selected as break is unselected
   * @param slot the selected slot id
   * @param index the index of the selected slot, needed to check subsequent slots
   */
  sendRemovedSlotId(slot: SlotDto, index: number, day: string) {
    this.selectedSlot.next({
      slot,
      index,
      day,
      removeSlot: true
    });
    slot.available = true;
  }

  /**
   * Returns the day name of the given slot
   * monday is index 0, add slot offset % 7 to get day name
   */
  getDayName(slotOffset: Date): string {
    if (!this.isSlotConfig) {
      return this.weekday[(this.today.getDay() + slotOffset.getDay() - 1) % 7];
    }
    return this.weekday[(slotOffset.getDay()) % 7];
  }
}
