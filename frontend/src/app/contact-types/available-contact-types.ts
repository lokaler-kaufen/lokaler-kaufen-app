export class ContactTypesEnum {
  static readonly availableContactTypes: string[] = [
    'WHATSAPP',
    'FACETIME',
    'GOOGLE_DUO',
    'PHONE',
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

  static getEnumValue(displayName): string {
    console.log('DisplayName: ' + displayName[0]);
    // we get an array of options but only one option can be selected
    switch (displayName[0]) {
      case 'WhatsApp':
        return 'WHATSAPP';
        break;
      case 'Telefon':
        return 'PHONE';
        break;
      case 'Facetime':
        return 'FACETIME';
        break;
      case 'Google Duo':
        return 'GOOGLE_DUO';
        break;
      case 'Skype':
        return 'SKYPE';
        break;
      case 'Signal':
        return 'SIGNAL';
        break;
      case 'Viber':
        return 'VIBER';
        break;
    }
  }
}
