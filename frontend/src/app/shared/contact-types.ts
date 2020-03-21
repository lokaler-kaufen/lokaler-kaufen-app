export class ContactTypes {
  static readonly POSSIBLE_CONTACT_TYPES = [
    'phone', 'facetime', 'whatsapp'
  ];

  // Replace '_' with ' ' and capitalize first letter
  static getDisplayName(contactType: string) {
    let splitted = contactType.split('_');
    splitted = splitted.map(split => {
      return split.charAt(0).toUpperCase() + split.slice(1);
    });
    return splitted.join(' ');
  }
}
