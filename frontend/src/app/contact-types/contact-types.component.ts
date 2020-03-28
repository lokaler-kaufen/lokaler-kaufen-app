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
    let splitted = contactType.split('_');
    splitted = splitted.map(split => {
      return split.charAt(0) + split.slice(1).toLowerCase();
    });
    return splitted.join(' ');
  }
}
