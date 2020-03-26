import { browser, by, element } from 'protractor';
import { PageObject } from './PageObject';

export class AppPage implements PageObject {

  private input = element(by.name('location'));

  private autocompleteSuggestions = element.all(by.css('.mat-autocomplete-panel mat-option'));

  async get(url) {
    return browser.get(url);
  }

  async getTitleText() {
    return element(by.css('app-root .content span')).getText();
  }

  async typeZipCode(zip: number) {
    return this.input.sendKeys(zip);
  }

  async getCurrentZipCode() {
    return this.input.getAttribute('value');
  }

  async getSuggestion(partialZip: number) {
    return this.autocompleteSuggestions.filter(async (option, index) => {
      const optionText = await option.element(by.className('mat-option-text')).getText();
      return optionText.includes('' + partialZip);
    });
  }

  async isSuggestionVisible(partialZip: number) {
    const desiredOption = await this.getSuggestion(partialZip);

    return desiredOption[0].isPresent();
  }

  async clickSuggestion(partialZip: number) {
    const desiredOption = await this.getSuggestion(partialZip);

    return desiredOption[0].click();
  }

}
