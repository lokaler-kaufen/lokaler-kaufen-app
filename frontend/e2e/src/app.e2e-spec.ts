import { AppPage } from './app.po';
import { browser, by, element, logging } from 'protractor';

describe('workspace-project App', () => {

  // Doc: https://angular.io/guide/testing
  // Major takeaway at the bottom of the page: E2E tests suck. Still, we might want some :)

  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('should display the logo', async () => {
    await page.get(browser.baseUrl);

    // either grab stuff directly in the test using "by" locators...
    const logo = element(by.css('app-root app-header img'));
    expect(logo.isDisplayed()).toBe(true);
  });

  it('should provide a working location form with autocomplete', async () => {
    await page.get(browser.baseUrl);

    // ... or let page objects do the heavy lifting (speeds up writing of actual tests)
    expect(page.getCurrentZipCode()).toBe('');

    await page.typeZipCode(8154);
    expect(page.isSuggestionVisible(81549)).toBe(true);

    await page.clickSuggestion(81549);

    expect(page.getCurrentZipCode()).toBe('81549');
  });

  // This is somewhat overkill and not needed all the time. It's a nice "our browser's console must be clean at all
  // times" checker, though.
  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE
    } as logging.Entry));
  });

});
