import {Component, Input} from '@angular/core';
import {ShopAdminDto} from '../data/client/model/shopAdminDto';
import ContactTypesEnum = ShopAdminDto.ContactTypesEnum;

@Component({
  selector: 'contact-types',
  templateUrl: './contact-types.component.html',
  styleUrls: ['./contact-types.component.css']
})
export class ContactTypesComponent {

  @Input()
  availableContactTypes: string[];

  contactTypes = Object.keys(ContactTypesEnum).map(key => ContactTypesEnum[key]);

  constructor() {
  }

  getEnumValue(contactType: any) {
    return contactType
      .split('_')
      .map(c => `${c.charAt(0)}${c.slice(1).toLowerCase()}`)
      .join(' ');
  }
}
