import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {NotificationsService} from 'angular2-notifications';
import {BusinessHours, setRightSlot} from '../shop-creation-page/shop-creation-page.component';
import {CreateShopDto, DayDto, ShopDetailDto, SlotConfigDto, UpdateShopDto} from '../data/client';
import ContactTypesEnum = ShopDetailDto.ContactTypesEnum;

@Component({
  selector: 'shop-management-page',
  templateUrl: './shop-management-page.component.html',
  styleUrls: ['./shop-management-page.component.css']
})
export class ShopManagementPageComponent implements OnInit {

  nameFormGroup: FormGroup;
  addressFormGroup: FormGroup;
  descriptionFormGroup: FormGroup;
  contactFormGroup = new FormGroup({});
  openingFormGroup = new FormGroup({});

  contactTypes;
  businessHours = BusinessHours;
  days;
  details: CreateShopDto = {};

  constructor(private client: HttpClient,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private matDialog: MatDialog,
              private notificationsService: NotificationsService) {
    this.days = Array.from(this.businessHours.POSSIBLE_BUSINESS_HOURS.keys());
    this.contactTypes = Object.keys(ContactTypesEnum).map(key => ContactTypesEnum[key]);
  }

  ngOnInit() {
    this.configureFormControls();
    this.client.get<CreateShopDto>('/api/shop/me')
      .subscribe((shopDetails: CreateShopDto) => {
          this.details = shopDetails;
          this.setConfiguredShopDetails();
        },
        error => {
          console.log('Error requesting shop details: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Es ist ein Fehler beim Laden der Details aufgetreten.');
        });
  }

  setConfiguredShopDetails() {
    this.nameFormGroup.controls.nameCtrl.setValue(this.details.ownerName);
    this.nameFormGroup.controls.businessNameCtrl.setValue(this.details.name);
    this.addressFormGroup.controls.streetCtrl.setValue(this.details.street);
    this.addressFormGroup.controls.zipCtrl.setValue(this.details.zipCode);
    this.addressFormGroup.controls.cityCtrl.setValue(this.details.city);
    this.addressFormGroup.controls.suffixCtrl.setValue(this.details.addressSupplement);
    this.descriptionFormGroup.controls.descriptionCtrl.setValue(this.details.details);
    this.descriptionFormGroup.controls.urlCtrl.setValue(this.details.website);
    this.contactTypes.forEach(contact => {
      const contactCtrl = contact.toLowerCase() + 'Ctrl';
      console.log('Ctrl name: ' + contactCtrl + ', value: ' + this.details.contactTypes[contact]);
      if (this.details.contactTypes[contact]) {
        this.contactFormGroup.get(contactCtrl).setValue(this.details.contactTypes[contact]);
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
      } else {
        opening.enabled = false;
        this.openingFormGroup.get(fromCtrl).setValue('09:00');
        this.openingFormGroup.get(toCtrl).setValue('16:00');
      }
    });
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
      suffixCtrl: '',
    });
    this.descriptionFormGroup = this.formBuilder.group({
      descriptionCtrl: ['', Validators.required],
      urlCtrl: ['', [Validators.pattern('(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?')]]
    });
    this.contactTypes.forEach(type => {
      const ctrl = type.toLowerCase() + 'Ctrl';
      this.contactFormGroup.addControl(ctrl, new FormControl(''));
    });
    Array.from(this.businessHours.POSSIBLE_BUSINESS_HOURS.keys()).forEach((day: string) => {
      const fromCtrl = day + 'FromCtrl';
      const toCtrl = day + 'ToCtrl';
      this.openingFormGroup.addControl(fromCtrl, new FormControl(''));
      this.openingFormGroup.addControl(toCtrl, new FormControl(''));
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
    this.contactTypes.forEach(contact => {
      const contactCtrl = contact.toLowerCase() + 'Ctrl';
      const value = this.contactFormGroup.get(contactCtrl).value;
      console.log('Contact type: ' + contact + ', value: ' + value);
      if (value) {
        availableContactTypes[contact] = value;
      }
    });
    updateShopDto.contactTypes = availableContactTypes;
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
    this.client.put('/api/shop', updateShopDto).subscribe(() => {
        this.router.navigate(['shops/']);
      },
      error => {
        console.log('Error updating shop: ' + error.status + ', ' + error.message);
        this.notificationsService.error('Tut uns leid!', 'Dein Laden konnte leider nicht aktualisiert werden.');
      });
  }

  getEnumValue(contactType: any) {
    let splitted = contactType.split('_');
    splitted = splitted.map(split => {
      return split.charAt(0) + split.slice(1).toLowerCase();
    });
    return splitted.join(' ');
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
