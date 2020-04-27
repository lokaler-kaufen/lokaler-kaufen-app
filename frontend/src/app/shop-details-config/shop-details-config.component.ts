import {Component, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {BusinessHours, setRightSlot} from '../shop-creation-page/shop-creation-page.component';
import {MatDialog} from '@angular/material/dialog';
import {Observable, ReplaySubject} from 'rxjs';
import {
  BreakDto,
  BreaksDto,
  DayDto,
  LocationSuggestionDto,
  ShopOwnerDetailDto,
  SlotConfigDto,
  SlotDto,
  UpdateShopDto
} from '../data/api';
import {ContactTypesEnum} from '../contact-types/available-contact-types';
import {filter} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {AsyncNotificationService} from '../i18n/async-notification.service';
import {StepperSelectionEvent} from '@angular/cdk/stepper';
import {ReserveSlotsData, SlotSelectionData} from '../slots/slots.component';
import {LocationClient} from '../api/location/location.client';
import {ReservationClient} from '../api/reservation/reservation.client';

export interface UpdateShopData {
  image: File;
  deleteImage: boolean;
  updateShopDto: UpdateShopDto;
  id: string;
}

export interface SlotBreakData {
  id: number;
  slot: SlotDto;
}

export interface SlotBreaksData {
  friday?: Array<SlotBreakData>;
  monday?: Array<SlotBreakData>;
  saturday?: Array<SlotBreakData>;
  sunday?: Array<SlotBreakData>;
  thursday?: Array<SlotBreakData>;
  tuesday?: Array<SlotBreakData>;
  wednesday?: Array<SlotBreakData>;
}

@Component({
  selector: 'shop-details',
  templateUrl: './shop-details-config.component.html',
  styleUrls: ['./shop-details-config.component.css']
})
export class ShopDetailsConfigComponent implements OnInit {

  constructor(private formBuilder: FormBuilder,
              private matDialog: MatDialog,
              private notification: AsyncNotificationService,
              private client: HttpClient,
              private location: LocationClient,
              private reservation: ReservationClient) {
    this.days = Array.from(this.businessHours.POSSIBLE_BUSINESS_HOURS.keys());
  }

  @Input()
  detailsObservable: Observable<ShopOwnerDetailDto>;

  @Input()
  sendUpdatedShopDetails: Observable<boolean>;

  @Output()
  updateShopEvent: ReplaySubject<UpdateShopData> = new ReplaySubject<UpdateShopData>();

  nameFormGroup: FormGroup;
  addressFormGroup: FormGroup;
  descriptionFormGroup: FormGroup;
  contactFormGroup: FormGroup;
  openingFormGroup: FormGroup;
  logoFormGroup: FormGroup;

  contactTypes = ContactTypesEnum;
  businessHours = BusinessHours;
  days;
  details: ShopOwnerDetailDto = {};
  citySuggestions: LocationSuggestionDto[] = [];

  image: File;
  localImageUrl: any;

  fileIsTooBig = false;
  wrongFileExtension = false;

  deleteImage = false;

  slotsPreview: ReplaySubject<ReserveSlotsData> = new ReplaySubject<ReserveSlotsData>();

  slotBreaks: SlotBreaksData = {
    monday: new Array<SlotBreakData>(),
    tuesday: new Array<SlotBreakData>(),
    wednesday: new Array<SlotBreakData>(),
    thursday: new Array<SlotBreakData>(),
    friday: new Array<SlotBreakData>(),
    saturday: new Array<SlotBreakData>(),
    sunday: new Array<SlotBreakData>()
  };

  private breaksChanged = false;

  ngOnInit() {
    this.detailsObservable
      .subscribe((shopDetails: ShopOwnerDetailDto) => {
          this.details = shopDetails;
          this.configureFormControls();
        },
        error => {
          console.log('Error requesting shop details: ' + error.status + ', ' + error.message);
          this.notification.error('shop.details-config.error.generic.title', 'shop.details-config.error.generic.message');
        });
    this.sendUpdatedShopDetails.subscribe((sendUpdatedShopDetails: boolean) => {
      if (sendUpdatedShopDetails) {
        this.updateShop();
      }
    });
  }

  configureFormControls() {
    this.nameFormGroup = this.formBuilder.group({
      nameCtrl: [this.details.ownerName, Validators.required],
      businessNameCtrl: [this.details.name, Validators.required]
    });
    this.addressFormGroup = this.formBuilder.group({
      streetCtrl: [this.details.street, Validators.required],
      zipCtrl: [this.details.zipCode, [Validators.required, Validators.pattern(new RegExp(/^\d{5}$/))]],
      cityCtrl: [this.details.city, Validators.required],
      suffixCtrl: this.details.addressSupplement
    });
    this.citySuggestions.push({placeName: this.details.city});
    this.addressFormGroup.get('zipCtrl').statusChanges.pipe(
      filter((status: string) => {
        console.log(status);
        this.citySuggestions = [];
        return status === 'VALID';
      }))
      .subscribe(() => this.onZipCodeValid());
    this.descriptionFormGroup = this.formBuilder.group({
      descriptionCtrl: [this.details.details, Validators.required],
      urlCtrl: [this.details.website, [Validators.pattern('(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?')]],
      facebookCtrl: [this.details.socialLinks.facebook, Validators.pattern('^(?!.*\\/).*$')],
      instagramCtrl: [this.details.socialLinks.instagram, Validators.pattern('^(?!.*\\/).*$')],
      twitterCtrl: [this.details.socialLinks.twitter, Validators.pattern('^(?!.*\\/).*$')],
    });
    this.contactFormGroup = new FormGroup({});
    this.contactTypes.availableContactTypes.forEach(type => {
      const ctrl = type.toLowerCase() + 'Ctrl';
      this.contactFormGroup.addControl(ctrl, new FormControl(this.details.contacts[type]));
    });
    this.contactFormGroup.setValidators(this.atLeastOneContact());
    this.openingFormGroup = new FormGroup({});
    Array.from(this.businessHours.POSSIBLE_BUSINESS_HOURS.keys()).forEach((day: string) => {
      const dayOpeningHours = this.getRightSlot(day, this.details.slots);
      const fromCtrl = day + 'FromCtrl';
      const toCtrl = day + 'ToCtrl';
      if (dayOpeningHours) {
        this.openingFormGroup.addControl(fromCtrl, new FormControl(dayOpeningHours.start, Validators.required));
        this.openingFormGroup.addControl(toCtrl, new FormControl(dayOpeningHours.end, Validators.required));
      } else {
        this.openingFormGroup.addControl(fromCtrl, new FormControl('09:00', Validators.required));
        this.openingFormGroup.get(fromCtrl).disable();
        this.openingFormGroup.addControl(toCtrl, new FormControl('16:00', Validators.required));
        this.openingFormGroup.get(toCtrl).disable();
      }
    });
    this.openingFormGroup.addControl('defaultCtrl', new FormControl(this.details.slots.timePerSlot));
    this.openingFormGroup.addControl('pauseCtrl', new FormControl(this.details.slots.timeBetweenSlots));
    this.openingFormGroup.addControl('delayCtrl', new FormControl(this.details.slots.delayForFirstSlot));
    this.logoFormGroup = this.formBuilder.group({
      autoColorCtrl: this.details.autoColorEnabled
    });
  }


  toggleAvailability(day: string): void {
    const businessHoursForDay = this.businessHours.POSSIBLE_BUSINESS_HOURS.get(day);
    businessHoursForDay.enabled = !businessHoursForDay.enabled;
    const fromCtrl = day + 'FromCtrl';
    const toCtrl = day + 'ToCtrl';
    if (businessHoursForDay.enabled) {
      this.openingFormGroup.controls[fromCtrl].enable();
      this.openingFormGroup.controls[toCtrl].enable();
    } else {
      this.openingFormGroup.controls[fromCtrl].disable();
      this.openingFormGroup.controls[toCtrl].disable();
    }
  }

  updateShop() {
    // If one form group is invalid, don't save the changes
    if (!this.addressFormGroup.valid || !this.contactFormGroup.valid || !this.descriptionFormGroup.valid ||
      !this.nameFormGroup.valid || !this.openingFormGroup.valid) {
      this.notification.error('shop.details-config.error.update.title', 'shop.details-config.error.update.message');
      return;
    }
    console.log('Update shop');
    const updateShopDto: UpdateShopDto = this.details as UpdateShopDto;
    updateShopDto.ownerName = this.nameFormGroup.get('nameCtrl').value;
    updateShopDto.name = this.nameFormGroup.get('businessNameCtrl').value;
    updateShopDto.street = this.addressFormGroup.get('streetCtrl').value;
    updateShopDto.zipCode = this.addressFormGroup.get('zipCtrl').value;
    updateShopDto.city = this.addressFormGroup.get('cityCtrl').value;
    updateShopDto.addressSupplement = this.addressFormGroup.get('suffixCtrl').value;
    updateShopDto.details = this.descriptionFormGroup.get('descriptionCtrl').value;
    updateShopDto.website = this.descriptionFormGroup.get('urlCtrl').value;
    updateShopDto.autoColorEnabled = this.logoFormGroup.get('autoColorCtrl').value;
    updateShopDto.socialLinks = {
      twitter: this.descriptionFormGroup.get('twitterCtrl').value,
      instagram: this.descriptionFormGroup.get('instagramCtrl').value,
      facebook: this.descriptionFormGroup.get('facebookCtrl').value
    };
    const availableContactTypes: { [key: string]: string; } = {};
    this.contactTypes.availableContactTypes.forEach(contact => {
      const contactCtrl = contact.toLowerCase() + 'Ctrl';
      const value = this.contactFormGroup.get(contactCtrl).value;
      if (value) {
        availableContactTypes[contact] = value;
      }
    });
    updateShopDto.contacts = availableContactTypes;
    let slots: SlotConfigDto = {};
    this.businessHours.POSSIBLE_BUSINESS_HOURS.forEach((opening, day) => {
      if (opening.enabled) {
        const fromCtrl = day + 'FromCtrl';
        const toCtrl = day + 'ToCtrl';
        slots = setRightSlot(day, this.openingFormGroup.get(fromCtrl).value, this.openingFormGroup.get(toCtrl).value, slots);
      }
    });
    slots.timeBetweenSlots = this.openingFormGroup.get('pauseCtrl').value;
    slots.timePerSlot = this.openingFormGroup.get('defaultCtrl').value;
    slots.delayForFirstSlot = this.openingFormGroup.get('delayCtrl').value;
    updateShopDto.slots = slots;
    if (this.breaksChanged) {
      updateShopDto.breaks = this.fillBreakConfig();
    }
    this.updateShopEvent.next({
      updateShopDto,
      id: this.details.id,
      image: this.image,
      deleteImage: this.deleteImage
    });
  }

  // Validation at least one contact type set
  private atLeastOneContact = () => {
    return (controlGroup) => {
      const controls = controlGroup.controls;
      if (controls) {
        const theOne = Object.keys(controls).find(key => controls[key].value !== '');
        if (!theOne) {
          return {
            atLeastOneRequired: {
              text: 'Geben Sie mindestens eine Kontaktmöglichkeit an.'
            }
          };
        }
      }
      return null;
    };
  }

  private onZipCodeValid() {
    const zipCode = this.addressFormGroup.get('zipCtrl').value;
    this.location.getSuggestions(zipCode)
      .then(response => {
        if (response.suggestions.length > 0) {
          this.citySuggestions = response.suggestions;
          this.addressFormGroup.get('cityCtrl').setValue(this.citySuggestions[0].placeName);
        } else {
          this.addressFormGroup.get('cityCtrl').setErrors({noCityFound: true});
        }
      })
      .catch(() => console.log('Error fetching cities to zip code'));
  }

  private fillSlotsConfig() {
    let slots: SlotConfigDto = {};
    this.businessHours.POSSIBLE_BUSINESS_HOURS.forEach((opening, day) => {
      if (opening.enabled) {
        const fromCtrl = day + 'FromCtrl';
        const toCtrl = day + 'ToCtrl';
        slots = setRightSlot(day, this.openingFormGroup.get(fromCtrl).value, this.openingFormGroup.get(toCtrl).value, slots);
      }
    });
    slots.timeBetweenSlots = this.openingFormGroup.get('pauseCtrl').value;
    slots.timePerSlot = this.openingFormGroup.get('defaultCtrl').value;
    slots.delayForFirstSlot = this.openingFormGroup.get('delayCtrl').value;
    return slots;
  }

  private fillBreakConfig() {
    const breaksDto: BreaksDto = {
      monday: new Array<BreakDto>(),
      tuesday: new Array<BreakDto>(),
      wednesday: new Array<BreakDto>(),
      thursday: new Array<BreakDto>(),
      friday: new Array<BreakDto>(),
      saturday: new Array<BreakDto>(),
      sunday: new Array<BreakDto>(),
    };
    Object.keys(this.slotBreaks).forEach(day => {
      const slots: SlotBreakData[] = this.slotBreaks[day];
      slots.sort((s1, s2) => {
        if (s1.id > s2.id) {
          return 1;
        }
        if (s1.id < s2.id) {
          return -1;
        }
        return 0;
      });
      if (slots.length > 0) {
        let oldSlotId = slots[0].id - 1;

        let start = slots[0].slot.start;
        let end = slots[0].slot.end;
        slots.forEach(slot => {
          if (oldSlotId + 1 === slot.id) {
            end = slot.slot.end;
          } else {
            breaksDto[day].push({start, end});
            start = slot.slot.start;
            end = slot.slot.end;
          }
          oldSlotId = slot.id;
        });
        breaksDto[day].push({start, end});
      }
    });
    return breaksDto;
  }

  changeBreakSlot($event: SlotSelectionData) {
    if ($event.removeSlot) {
      this.slotBreaks[$event.day] = this.slotBreaks[$event.day].filter(slotData => slotData.id !== $event.index);
    } else {
      this.slotBreaks[$event.day].push({
        slot: $event.slot,
        id: $event.index
      });
    }
  }

  previewSlots($event: StepperSelectionEvent) {
    if ($event.selectedIndex !== 5) {
      return;
    }

    const request = this.fillSlotsConfig();

    this.reservation.previewSlots(request)
      .then(slots => {
        this.slotsPreview.next({slots, breaks: this.details.breaks});
      });
    this.breaksChanged = true;
  }

  onFileChanged(event) {
    const file = event.target.files[0];
    console.log(file.type);
    // not supported file type
    if (!['image/png', 'image/jpeg'].includes(file.type)) {
      this.wrongFileExtension = true;
      return;
    }
    // max. size 5 MB
    if (file.size > 5242880) {
      this.fileIsTooBig = true;
      return;
    }
    this.image = file;
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = (readerEvent) => {
      this.localImageUrl = reader.result;
    };
    this.fileIsTooBig = false;
    this.wrongFileExtension = false;
    this.deleteImage = false;
  }

  private getRightSlot(day: string, slots: SlotConfigDto): DayDto {
    switch (day) {
      case 'Montag':
        return slots.monday;

      case 'Dienstag':
        return slots.tuesday;

      case 'Mittwoch':
        return slots.wednesday;

      case 'Donnerstag':
        return slots.thursday;

      case 'Freitag':
        return slots.friday;

      case 'Samstag':
        return slots.saturday;

      case 'Sonntag':
        return slots.sunday;

    }
  }

  onDeleteFile() {
    this.details.imageUrl = null;
    this.image = null;
    this.localImageUrl = null;
    this.deleteImage = true;
  }
}
