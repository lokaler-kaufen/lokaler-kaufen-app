import {Component} from '@angular/core';
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private matIconRegistry: MatIconRegistry, private domSanitizer: DomSanitizer) {
    this.matIconRegistry.addSvgIcon(
      'WhatsApp',
      this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/whatsapp.svg'))
      .addSvgIcon(
        'Voice Chat',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/voice_chat.svg'))
      .addSvgIcon(
        'Phone',
        this.domSanitizer.bypassSecurityTrustResourceUrl('../assets/phone.svg')
      );
  }
}
