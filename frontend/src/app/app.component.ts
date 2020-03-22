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
      'whatsapp',
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/whatsapp.svg'))
      .addSvgIcon(
        'facetime',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/voice_chat.svg'))
      .addSvgIcon(
        'phone',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/phone.svg')
      );
  }
}
