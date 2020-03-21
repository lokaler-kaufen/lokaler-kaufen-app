enum ContactType {
  PHONE = 'Phone',
  WHATSAPP = 'WhatsApp',
  FACETIME = 'Facetime'
}

export class ContactTypes {
  static readonly POSSIBLE_CONTACT_TYPES = Object.keys(ContactType);

  static getDisplayName(contactType: string) {
    return ContactType[contactType];
  }
}
