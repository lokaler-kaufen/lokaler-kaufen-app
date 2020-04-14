import {Component} from '@angular/core';
import {MessageOfTheDay} from './message-of-the-day';

@Component({
  selector: 'page-footer',
  templateUrl: './page-footer.component.html',
  styleUrls: ['./page-footer.component.css']
})
export class PageFooterComponent {
  randomMessageOfTheDay: string;

  constructor() {
    // Just fetch one random MOTD. Invoking MessageOfTheDay.getMessage()
    // directly will render a different quote on every render update.
    this.randomMessageOfTheDay = MessageOfTheDay.getMessage();
  }

  get messageOfTheDay(): string {
    return this.randomMessageOfTheDay;
  }
}
