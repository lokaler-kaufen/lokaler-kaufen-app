import {Component} from '@angular/core';
import {MatIconRegistry} from '@angular/material/icon';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private matIconRegistry: MatIconRegistry, private domSanitizer: DomSanitizer) {
    this.matIconRegistry.addSvgIcon(
      'WHATSAPP',
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/whatsapp.svg'))
      .addSvgIcon(
        'FACETIME',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/voice_chat.svg'))
      .addSvgIcon(
        'PHONE',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/phone.svg'))
      .addSvgIcon(
        'GOOGLE_DUO',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/google_duo.svg'))
      .addSvgIcon(
        'SKYPE',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/skype.svg'))
      .addSvgIcon(
        'VIBER',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/viber.svg'))
      .addSvgIcon(
        'SIGNAL',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/signal.svg')
      );
  }

  isIEOrEdge = /msie\s|trident\/|edge\//i.test(window.navigator.userAgent)
}
