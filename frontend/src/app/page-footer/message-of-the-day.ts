export class MessageOfTheDay {
  private static readonly _MESSAGES: string[] = [
    'Schöner kann einkaufen (derzeit) nicht sein.',
    'Von der Couch einkaufen als wär man vor Ort.',
    '#supportyourlocal und #stayhome zugleich.',
    'Bequem wie die Großen, persönlich wie die Kleinen.',
    'Zurücklehnen statt Warteschleife!',
    'Persönlich in jeden Laden und trotzdem das Haus nicht verlassen.',
    'Nirgendwo ist einkaufen so schön wie daheim.',
    'Konatktlos heißt nicht unpersönlich.',
    'Gut beraten, wie im Laden.',
    'Homeshopping statt Teleshopping.',
    'Persönlich, herzlich, nah.',
    'Kurze Wege statt Luftpost.',
    'Logisch statt logistisch.',
    'Wissen, wo es herkommt.',
    'Lastenrad statt LKW.',
    'Wir halten zusammen.',
    'Mit den besten Empfehlungen.',
    'Vom Anzug bis zum Zeichenblock, alles gibts lokaler.',
    'Lieblingsladen statt Zentrallager.',
    'Das Homeoffice des Einkaufens.',
  ];

  static getMessage(): string {
    return MessageOfTheDay._MESSAGES[this.getRandomInt(MessageOfTheDay._MESSAGES.length)];
  }

  private static getRandomInt(max: number) {
    return Math.floor(Math.random() * Math.floor(max));
  }
}
