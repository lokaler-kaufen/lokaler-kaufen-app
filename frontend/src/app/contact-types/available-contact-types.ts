export class ContactTypesEnum {
  static readonly availableContactTypes: string[] = [
    'WHATSAPP',
    'PHONE',
    'FACETIME',
    'GOOGLE_DUO',
    'SKYPE',
    'SIGNAL',
    'VIBER'
  ];

  static getDisplayName(contactType: string): string {
    switch (contactType) {
      case 'WHATSAPP':
        return 'WhatsApp';
        break;
      case 'PHONE':
        return 'Telefon';
        break;
      case 'FACETIME':
        return 'Facetime';
        break;
      case 'GOOGLE_DUO':
        return 'Google Duo';
        break;
      case 'SKYPE':
        return 'Skype';
        break;
      case 'SIGNAL':
        return 'Signal';
        break;
      case 'VIBER':
        return 'Viber';
        break;
    }
  }
}
