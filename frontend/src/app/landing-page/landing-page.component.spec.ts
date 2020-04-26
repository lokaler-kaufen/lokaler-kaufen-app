import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LandingPageComponent} from './landing-page.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ZipCodeCacheService} from './zip-code-cache.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {NotificationsService, SimpleNotificationsModule} from 'angular2-notifications';
import {MatDialogModule} from '@angular/material/dialog';
import {TranslateModule} from '@ngx-translate/core';

/**
 * Overriding the ZipCodeCacheService to prevent cross-test leaking through the browser's localStorage.
 */
class MockZipCodeCacheService {

  private zipCode = '';

  setZipCode(zipCode: string) {
    this.zipCode = zipCode;
  }

  getZipCode(): string {
    return this.zipCode;
  }

}

describe('LandingPageComponent', () => {
  let component: LandingPageComponent;
  let fixture: ComponentFixture<LandingPageComponent>;
  let httpMock;
  let zipCodeCacheService: MockZipCodeCacheService;

  beforeEach(async(() => {
    zipCodeCacheService = new MockZipCodeCacheService();
    zipCodeCacheService.setZipCode('');

    return TestBed.configureTestingModule({
      declarations: [LandingPageComponent],
      imports: [
        ReactiveFormsModule,
        FormsModule,
        HttpClientTestingModule,
        RouterTestingModule,
        MatAutocompleteModule,
        MatDialogModule,
        SimpleNotificationsModule.forRoot(),
        TranslateModule.forRoot()
      ],
      providers: [
        {provide: ZipCodeCacheService, useValue: zipCodeCacheService},
        NotificationsService
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LandingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should disable the submit button initially', () => {
    expect(component.startDisabled).toBeTrue();
  });

  it('should enable the submit button when the user entered a valid zip code', () => {
    component.form.controls.zipCode.setValue('81549');

    expect(component.startDisabled).toBeFalse();
  });


  it('should enable the submit button if there is a valid cached zipCode', () => {
    zipCodeCacheService.setZipCode('12345');

    component.ngOnInit();

    expect(component.startDisabled).toBeFalse();
  });

});
