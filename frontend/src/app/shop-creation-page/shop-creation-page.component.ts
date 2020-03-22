import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ShopCreateDto} from '../data/client/model/shopCreateDto';
import ContactTypesEnum = ShopCreateDto.ContactTypesEnum;

export class OpeningHours {
  constructor(enabled: boolean = true, from: string = '09:00', to: string = '16:00') {
    this.enabled = enabled;
    this.from = from;
    this.to = to;
  }

  enabled: boolean;
  from: string;
  to: string;
}

export class BusinessHours {
  static readonly POSSIBLE_BUSINESS_HOURS = new Map([
    ['Montag', new OpeningHours()],
    ['Dienstag', new OpeningHours()],
    ['Mittwoch', new OpeningHours()],
    ['Donnerstag', new OpeningHours()],
    ['Freitag', new OpeningHours()],
    ['Samstag', new OpeningHours(false)],
    ['Sonntag', new OpeningHours(false)]
  ]);
}

@Component({
  selector: 'shop-creation-page',
  templateUrl: './shop-creation-page.component.html',
  styleUrls: ['./shop-creation-page.component.css']
})
export class ShopCreationPageComponent implements OnInit {
  nameFormGroup: FormGroup;
  addressFormGroup: FormGroup;
  descriptionFormGroup: FormGroup;
  contactFormGroup = new FormGroup({});
  openingFormGroup = new FormGroup({});
  passwordFormGroup: FormGroup;

  token: string;
  contactTypes;
  hidePassword = true;

  businessHours = BusinessHours;

  days;

  constructor(private formBuilder: FormBuilder) {
    this.days = Array.from(this.businessHours.POSSIBLE_BUSINESS_HOURS.keys());
    this.contactTypes = Object.keys(ContactTypesEnum).map(key => ContactTypesEnum[key]);
  }

  ngOnInit() {
    console.log('Contact Types size ' + this.contactTypes);
    this.nameFormGroup = this.formBuilder.group({
      nameCtrl: ['', Validators.required],
      businessNameCtrl: ['', Validators.required]
    });
    this.addressFormGroup = this.formBuilder.group({
      streetCtrl: ['', Validators.required],
      zipCtrl: ['', Validators.required],
      cityCtrl: ['', Validators.required],
      suffixCtrl: '',
    });
    this.descriptionFormGroup = this.formBuilder.group({
      descriptionCtrl: ['', Validators.required],
      urlCtrl: ['', [Validators.required, Validators.pattern('(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?')]]
    });
    this.contactTypes.forEach(type => {
      const ctrl = type.toLowerCase() + 'Ctrl';
      this.contactFormGroup.addControl(ctrl, new FormControl(''));
    });
    // this.openingFormGroup = this.formBuilder.group({});
    Array.from(this.businessHours.POSSIBLE_BUSINESS_HOURS.keys()).forEach((day: string) => {
      const fromCtrl = day + 'FromCtrl';
      const toCtrl = day + 'ToCtrl';
      this.openingFormGroup.addControl(fromCtrl, new FormControl(''));
      this.openingFormGroup.addControl(toCtrl, new FormControl(''));
      // console.log('Day: ' + day);
      this.openingFormGroup.controls[fromCtrl].setValue('09:00');
      this.openingFormGroup.controls[toCtrl].setValue('16:00');
    });
    this.passwordFormGroup = this.formBuilder.group({
      passwordCtrl: ['', [Validators.required, Validators.minLength(14)]],
      confirmPasswordCtrl: ['', Validators.required]
    }, {validator: this.checkMatchingPasswords('passwordCtrl', 'confirmPasswordCtrl')});
  }

  // Validation password equals confirmed password
  checkMatchingPasswords(passwordKey: string, passwordConfirmationKey: string) {
    return (group: FormGroup) => {
      const passwordInput = group.controls[passwordKey],
        passwordConfirmationInput = group.controls[passwordConfirmationKey];
      if (passwordInput.value !== passwordConfirmationInput.value) {
        return passwordConfirmationInput.setErrors({notEquivalent: true});
      } else {
        return passwordConfirmationInput.setErrors(null);
      }
    };
  }

  toggleAvailability(day: string): void {
    console.log('Toggle availability for day: ' + day);
    const businessHoursForDay = this.businessHours.POSSIBLE_BUSINESS_HOURS.get(day);
    businessHoursForDay.enabled = !businessHoursForDay.enabled;
    console.log(businessHoursForDay);
  }

  createShop() {
    let shopCreateDto: ShopCreateDto;
    shopCreateDto.ownerName = this.nameFormGroup.get('nameCtrl').value;
    shopCreateDto.name = this.nameFormGroup.get('businessNameCtrl').value;
    shopCreateDto.street = this.addressFormGroup.get('streetCtrl').value;
    shopCreateDto.zipCode = this.addressFormGroup.get('zipCtrl').value;
    shopCreateDto.city = this.addressFormGroup.get('cityCtrl').value;
    shopCreateDto.addressSupplement = this.addressFormGroup.get('suffixCtrl').value;
    // shopCreateDto.description = this.descriptionFormGroup.get('descriptionCtrl').value;
    // shopCreateDto.url = this.descriptionFormGroup.get('urlCtrl').value;
    let availableContactTypes: ContactTypesEnum[];
    this.contactTypes.forEach(contact => {
      const contactCtrl = contact + 'Ctrl';
      if (this.contactFormGroup.get(contactCtrl).value) {
        // TODO: Add contactType
      }
    });
  }

  getEnumValue(contactType: any) {
    let splitted = contactType.split('_');
    splitted = splitted.map(split => {
      return split.charAt(0) + split.slice(1).toLowerCase();
    });
    return splitted.join(' ');
  }
}
