import {Component, Input} from '@angular/core';

@Component({
  selector: 'shop-logo',
  template: `<img [ngClass]="logoClass" [src]="src" alt="Shop logo" [attr.loading]="'lazy'" class="shop-logo">`,
  styleUrls: ['./shop-logo.component.css'],
})
export class ShopLogoComponent {

  @Input()
  imageUrl: string;

  get logoClass() {
    return !!this.imageUrl ? 'shop-logo' : 'default-logo';
  }

  get src() {
    return !!this.imageUrl ? this.imageUrl : 'assets/default-logo.svg';
  }

}
