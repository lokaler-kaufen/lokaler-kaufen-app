import {Component} from '@angular/core';
import {MatIconRegistry} from '@angular/material/icon';
import {DomSanitizer} from '@angular/platform-browser';

const additionalIcons: Array<{ id: string, asset: string; }> = [
  {id: 'WHATSAPP', asset: '../assets/whatsapp.svg'},
  {id: 'FACETIME', asset: '../assets/voice_chat.svg'},
  {id: 'PHONE', asset: '../assets/phone.svg'},
  {id: 'GOOGLE_DUO', asset: '../assets/google_duo.svg'},
  {id: 'SKYPE', asset: '../assets/skype.svg'},
  {id: 'VIBER', asset: '../assets/viber.svg'},
  {id: 'SIGNAL', asset: '../assets/signal.svg'}
];

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private matIconRegistry: MatIconRegistry, private domSanitizer: DomSanitizer) {
    additionalIcons.forEach(({asset, id}) => {
      this.matIconRegistry.addSvgIcon(id, this.domSanitizer.bypassSecurityTrustResourceUrl(asset));
    });
  }

  isIEOrEdge = /msie\s|trident\/|edge\//i.test(window.navigator.userAgent);

}
