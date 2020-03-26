import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {LandingPageComponent} from './landing-page.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ZipCodeCacheService} from './zip-code-cache.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {MatAutocompleteModule} from '@angular/material/autocomplete';

describe('LandingPageComponent', () => {
  let component: LandingPageComponent;
  let fixture: ComponentFixture<LandingPageComponent>;
  let httpMock;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LandingPageComponent],
      imports: [ReactiveFormsModule, FormsModule, HttpClientTestingModule, RouterTestingModule, MatAutocompleteModule],
      providers: [ZipCodeCacheService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    // clear zip code cache
    new ZipCodeCacheService().setZipCode('');

    fixture = TestBed.createComponent(LandingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should disable start initially', () => {
    new ZipCodeCacheService().setZipCode('');

    component.ngOnInit();

    expect(component.startDisabled).toBeTrue();
  });

  it('should enable start if there is a valid cached zipCode', () => {
    new ZipCodeCacheService().setZipCode('12345');

    component.ngOnInit();

    expect(component.startDisabled).toBeFalse();
  });

  it('should enable start on input with location hits', fakeAsync(() => {
    new ZipCodeCacheService().setZipCode('');
    component.ngOnInit();
    expect(component.startDisabled).toBeTrue();

    component.form.controls.zipCode.setValue('83024');
    tick(300);

    const req = httpMock.expectOne('/api/location/suggestion?zipCode=83024');
    req.flush({suggestions: [{countryCode: 'DE', zipCode: '83024', placeName: 'Rosenheim'}]});

    expect(component.startDisabled).toBeFalse();
  }));


  it('should disable start on input without location hits', fakeAsync(() => {
    new ZipCodeCacheService().setZipCode('');
    component.ngOnInit();
    expect(component.startDisabled).toBeTrue();

    component.form.controls.zipCode.setValue('83021');
    tick(200);

    const req = httpMock.expectOne('/api/location/suggestion?zipCode=83021');
    req.flush({suggestions: []});

    expect(component.startDisabled).toBeTrue();
  }));

});
