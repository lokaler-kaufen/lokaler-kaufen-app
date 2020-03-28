import {Component, Input} from '@angular/core';
import {ContactTypesEnum} from './available-contact-types';

@Component({
  selector: 'contact-types',
  templateUrl: './contact-types.component.html',
  styleUrls: ['./contact-types.component.css']
})
export class ContactTypesComponent {

  @Input()
  availableContactTypes: string[];

  contactTypes = ContactTypesEnum;

  constructor() {
  }

}
