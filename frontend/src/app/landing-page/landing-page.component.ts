import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {debounceTime, filter} from 'rxjs/operators';
import {Router} from '@angular/router';
import {ZipCodeCacheService} from './zip-code-cache.service';
import {LocationSuggestionDto} from '../data/api';
import {BreakpointObserver} from '@angular/cdk/layout';
import {AsyncNotificationService} from '../i18n/async-notification.service';
import {LocationClient} from '../api/location/location.client';

@Component({
  selector: 'landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {

  private zipCodeInitialValue: string | null;

  suggestions: LocationSuggestionDto[] = [];

  form: FormGroup;

  isSmallScreen: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private client: LocationClient,
    private router: Router,
    private zipCodeCacheService: ZipCodeCacheService,
    breakpointObserver: BreakpointObserver,
    private asyncNS: AsyncNotificationService
  ) {
    // listen to responsive breakpoint changes
    breakpointObserver.observe('(max-width: 719px)').subscribe(
      result => this.isSmallScreen = result.matches
    );
  }

  ngOnInit(): void {
    this.zipCodeInitialValue = this.zipCodeCacheService.getZipCode();

    this.form = this.formBuilder.group({
      // pre-fill with value from cache (if present)
      zipCode: [this.zipCodeInitialValue, [Validators.required, Validators.pattern(new RegExp(/^\d{5}$/))]]
    });

    // clear completion if no user input
    this.form.controls.zipCode.valueChanges.subscribe(value => {
      if (!value) {
        this.suggestions = [];
      }
    });

    this.form.controls.zipCode.valueChanges
      .pipe(
        debounceTime(150),
        filter(value => !!value)
      )
      .subscribe(value => {
        this.client.getSuggestions(value)
          .then(({suggestions}) => this.suggestions = suggestions)
          .catch(() => this.suggestions = []);
      });
  }

  checkZipCodeInput($event) {
    const input = $event.target as HTMLInputElement;

    const originalValue: string = input.value;
    const maxLength = +input.getAttribute('maxlength');

    let value = originalValue.replace(new RegExp(/[^\d]/g), '');
    if (value.length > maxLength) {
      value = value.slice(0, maxLength);
    }

    input.value = value;
  }

  get startDisabled(): boolean {
    return !this.form.valid;
  }

  start(): void {
    const locationFromInput = this.zipCodeFromInput;

    // cache the zipCode for later
    this.zipCodeCacheService.setZipCode(locationFromInput);

    this.client.isLocationKnown(locationFromInput)
      .then(() => {
        this.router.navigate(['/shops'], {queryParams: {location: locationFromInput}});
      })
      .catch(error => {
        if (error.status === 404) {
          this.asyncNS.error('landing.invalidZip.title', 'landing.invalidZip.content');

        } else {
          this.asyncNS.error('landing.locationApiError.title', 'landing.locationApiError.content');
        }
      });
  }

  private get zipCodeFromInput(): string {
    return this.form.controls.zipCode.value;
  }

}
