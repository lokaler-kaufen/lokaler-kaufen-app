import {ShopDetailDto} from '../data/client';
import ContactTypesEnum = ShopDetailDto.ContactTypesEnum;

enum ContactType {
  PHONE = 'Phone',
  WHATSAPP = 'WhatsApp',
  FACETIME = 'Facetime'
}

export class ContactTypes {
  static readonly POSSIBLE_CONTACT_TYPES = Object.keys(ContactTypesEnum);

  static getDisplayName(contactType: string) {
    let splitted = contactType.split('_');
    splitted = splitted.map(split => {
      return split.charAt(0) + split.slice(1).toLowerCase();
    });
    return splitted.join(' ');  }
}
