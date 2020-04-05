import {Component, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {BusinessHours, setRightSlot} from '../shop-creation-page/shop-creation-page.component';
import {MatDialog} from '@angular/material/dialog';
import {NotificationsService} from 'angular2-notifications';
import {Observable, ReplaySubject} from 'rxjs';
import {
  DayDto,
  LocationSuggestionDto,
  LocationSuggestionsDto,
  ShopOwnerDetailDto,
  SlotConfigDto,
  UpdateShopDto
} from '../data/api';
import {ContactTypesEnum} from '../contact-types/available-contact-types';
import {filter} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {ImageService} from '../shared/image.service';

export interface UpdateShopData {
  image: File;
  updateShopDto: UpdateShopDto;
  id: string;
}

export interface ShopImage {
  data: File,
  progress: number
}

@Component({
  selector: 'shop-details',
  templateUrl: './shop-details-config.component.html',
  styleUrls: ['./shop-details-config.component.css']
})
export class ShopDetailsConfigComponent implements OnInit {

  @Input()
  detailsObservable: Observable<ShopOwnerDetailDto>;

  @Input()
  sendUpdatedShopDetails: Observable<boolean>;

  @Output()
  updateShopEvent: ReplaySubject<UpdateShopData> = new ReplaySubject<UpdateShopData>();

  nameFormGroup: FormGroup;
  addressFormGroup: FormGroup;
  descriptionFormGroup: FormGroup;
  contactFormGroup = new FormGroup({});
  openingFormGroup = new FormGroup({});

  contactTypes = ContactTypesEnum;
  businessHours = BusinessHours;
  days;
  details: ShopOwnerDetailDto = {};
  citySuggestions: LocationSuggestionDto[] = [];

  image: ShopImage = {data: null, progress: 0};

  constructor(private formBuilder: FormBuilder,
              private matDialog: MatDialog,
              private notificationsService: NotificationsService,
              private client: HttpClient,
              private imageService: ImageService) {
    this.days = Array.from(this.businessHours.POSSIBLE_BUSINESS_HOURS.keys());
  }

  ngOnInit() {
    this.configureFormControls();
    this.detailsObservable
      .subscribe((shopDetails: ShopOwnerDetailDto) => {
          this.details = shopDetails;
          this.setConfiguredShopDetails();
        },
        error => {
          console.log('Error requesting shop details: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Details aufgetreten.');
        });
    this.sendUpdatedShopDetails.subscribe((sendUpdatedShopDetails: boolean) => {
      if (sendUpdatedShopDetails) {
        this.updateShop();
      }
    });
  }

  setConfiguredShopDetails() {
    this.nameFormGroup.controls.nameCtrl.setValue(this.details.ownerName);
    this.nameFormGroup.controls.businessNameCtrl.setValue(this.details.name);
    this.addressFormGroup.controls.streetCtrl.setValue(this.details.street);
    this.addressFormGroup.controls.zipCtrl.setValue(this.details.zipCode);
    this.addressFormGroup.controls.cityCtrl.setValue(this.details.city);
    this.addressFormGroup.controls.suffixCtrl.setValue(this.details.addressSupplement);
    this.citySuggestions.push({placeName: this.details.city});
    this.addressFormGroup.get('zipCtrl').statusChanges.pipe(
      filter((status: string) => {
        console.log(status);
        this.citySuggestions = [];
        return status === 'VALID';
      }))
      .subscribe(() => this.onZipCodeValid());
    this.descriptionFormGroup.controls.descriptionCtrl.setValue(this.details.details);
    this.descriptionFormGroup.controls.urlCtrl.setValue(this.details.website);
    this.contactTypes.availableContactTypes.forEach(contact => {
      const contactCtrl = contact.toLowerCase() + 'Ctrl';
      if (this.details.contacts[contact]) {
        this.contactFormGroup.get(contactCtrl).setValue(this.details.contacts[contact]);
      }
    });
    this.businessHours.POSSIBLE_BUSINESS_HOURS.forEach((opening, day) => {
      const dayOpeningHours = this.getRightSlot(day, this.details.slots);
      const fromCtrl = day + 'FromCtrl';
      const toCtrl = day + 'ToCtrl';
      // unavailable slots are null
      if (dayOpeningHours) {
        this.openingFormGroup.get(fromCtrl).setValue(dayOpeningHours.start);
        this.openingFormGroup.get(toCtrl).setValue(dayOpeningHours.end);
        this.openingFormGroup.get(fromCtrl).enable();
        this.openingFormGroup.get(toCtrl).enable();
        opening.enabled = true;
      } else {
        opening.enabled = false;
        this.openingFormGroup.get(fromCtrl).setValue('09:00');
        this.openingFormGroup.get(toCtrl).setValue('16:00');
        this.openingFormGroup.get(fromCtrl).disable();
        this.openingFormGroup.get(toCtrl).disable();
      }
    });
    this.openingFormGroup.controls.defaultCtrl.setValue(this.details.slots.timePerSlot);
    this.openingFormGroup.controls.pauseCtrl.setValue(this.details.slots.timeBetweenSlots);
  }

  configureFormControls() {
    this.nameFormGroup = this.formBuilder.group({
      nameCtrl: ['', Validators.required],
      businessNameCtrl: ['', Validators.required]
    });
    this.addressFormGroup = this.formBuilder.group({
      streetCtrl: ['', Validators.required],
      zipCtrl: ['', [Validators.required, Validators.pattern(new RegExp(/^\d{5}$/))]],
      cityCtrl: ['', Validators.required],
      suffixCtrl: ''
    });
    this.descriptionFormGroup = this.formBuilder.group({
      descriptionCtrl: ['', Validators.required],
      urlCtrl: ['', [Validators.pattern('(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?')]]
    });
    this.contactTypes.availableContactTypes.forEach(type => {
      const ctrl = type.toLowerCase() + 'Ctrl';
      this.contactFormGroup.addControl(ctrl, new FormControl(''));
    });
    this.contactFormGroup.setValidators(this.atLeastOneContact());
    Array.from(this.businessHours.POSSIBLE_BUSINESS_HOURS.keys()).forEach((day: string) => {
      const fromCtrl = day + 'FromCtrl';
      const toCtrl = day + 'ToCtrl';
      this.openingFormGroup.addControl(fromCtrl, new FormControl('', Validators.required));
      this.openingFormGroup.addControl(toCtrl, new FormControl('', Validators.required));
      this.openingFormGroup.controls[fromCtrl].setValue('09:00');
      this.openingFormGroup.controls[toCtrl].setValue('16:00');
      this.openingFormGroup.addControl('defaultCtrl', new FormControl(''));
      this.openingFormGroup.addControl('pauseCtrl', new FormControl(''));
      this.openingFormGroup.controls.defaultCtrl.setValue(15);
      this.openingFormGroup.controls.pauseCtrl.setValue(5);
    });
    // disable default disabled saturday and sunday controls
    this.openingFormGroup.controls.SamstagFromCtrl.disable();
    this.openingFormGroup.controls.SonntagFromCtrl.disable();
    this.openingFormGroup.controls.SamstagToCtrl.disable();
    this.openingFormGroup.controls.SonntagToCtrl.disable();
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
      this.notificationsService.error('Ungültige Eingabe', 'Bitte überprüfen Sie Ihre Änderungen nochmal.');
      return;
    }
    console.log('Update shop');
    const updateShopDto: UpdateShopDto = {};
    updateShopDto.ownerName = this.nameFormGroup.get('nameCtrl').value;
    updateShopDto.name = this.nameFormGroup.get('businessNameCtrl').value;
    updateShopDto.street = this.addressFormGroup.get('streetCtrl').value;
    updateShopDto.zipCode = this.addressFormGroup.get('zipCtrl').value;
    updateShopDto.city = this.addressFormGroup.get('cityCtrl').value;
    updateShopDto.addressSupplement = this.addressFormGroup.get('suffixCtrl').value;
    updateShopDto.details = this.descriptionFormGroup.get('descriptionCtrl').value;
    updateShopDto.website = this.descriptionFormGroup.get('urlCtrl').value;
    const availableContactTypes: { [key: string]: string; } = {};
    this.contactTypes.availableContactTypes.forEach(contact => {
      const contactCtrl = contact.toLowerCase() + 'Ctrl';
      const value = this.contactFormGroup.get(contactCtrl).value;
      console.log('Contact type: ' + contact + ', value: ' + value);
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
        console.log('FromCtrl: ' + fromCtrl);
        slots = setRightSlot(day, this.openingFormGroup.get(fromCtrl).value, this.openingFormGroup.get(toCtrl).value, slots);
      }
    });
    slots.timeBetweenSlots = this.openingFormGroup.get('pauseCtrl').value;
    slots.timePerSlot = this.openingFormGroup.get('defaultCtrl').value;
    updateShopDto.slots = slots;
    this.updateShopEvent.next({
      updateShopDto,
      id: this.details.id,
      image: this.image.data
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
  };

  private onZipCodeValid() {
    const zipCode = this.addressFormGroup.get('zipCtrl').value;
    this.client.get<LocationSuggestionsDto>('/api/location/suggestion?zipCode=' + zipCode).toPromise()
      .then(response => {
        if (response.suggestions.length > 0) {
          this.citySuggestions = response.suggestions;
          this.addressFormGroup.get('cityCtrl').setValue(this.citySuggestions[0].placeName);
        } else {
          this.addressFormGroup.get('cityCtrl').setErrors({noCityFound: true});
        }
      })
      .catch(error => console.log('Error fetching cities to zip code'));
  }

  fileIsTooBig: boolean = false;
  wrongFileExtension: boolean = false;

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
    this.image.data = file;
    this.fileIsTooBig = false;
    this.wrongFileExtension = false;
  }

  private getRightSlot(day: string, slots: SlotConfigDto): DayDto {
    switch (day) {
      case 'Montag':
        return slots.monday;
        break;
      case 'Dienstag':
        return slots.tuesday;
        break;
      case 'Mittwoch':
        return slots.wednesday;
        break;
      case 'Donnerstag':
        return slots.thursday;
        break;
      case 'Freitag':
        return slots.friday;
        break;
      case 'Samstag':
        return slots.saturday;
        break;
      case 'Sonntag':
        return slots.sunday;
        break;
    }
  }

}
