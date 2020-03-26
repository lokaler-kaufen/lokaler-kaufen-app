import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LocationControllerService, LocationSuggestionDto, LocationSuggestionsDto} from '../data/client';
import {debounceTime, map, switchMap} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {ZipCodeCacheService} from './zip-code-cache.service';

@Component({
  selector: 'landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {
  location: string;
  private zipCodeInitialValue: string;
  suggestions: LocationSuggestionDto[] = [];
  form: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private locationControllerService: LocationControllerService,
              private client: HttpClient,
              private router: Router,
              private zipCodeCacheService: ZipCodeCacheService) {
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
        switchMap(value => {
          if (value) {
            return this.client.get<LocationSuggestionsDto>('/api/location/suggestion?zipCode=' + value)
              .pipe(map((response) => response.suggestions));
          } else {
            return [];
          }
        })).subscribe(suggestions => this.suggestions = suggestions);
  }

  public get startDisabled(): boolean {
    return !this.startEnabled;
  }

  private get startEnabled(): boolean {
    // form has to be valid (of course), if it is:
    // - we allow to click start if the value was unchanged (this way the user can research fast)
    //    or
    // - if the changed zip code has hits
    return this.form.valid && this.inputUnchangedOrHasSuggestionHits;
  }

  private get inputUnchangedOrHasSuggestionHits(): boolean {
    return this.inputUnchanged || this.hasSuggestionHits;
  }

  private get inputUnchanged(): boolean {
    return this.zipCodeInitialValue === this.zipCodeFromInput;
  }

  private get hasSuggestionHits(): boolean {
    return this.suggestions.length > 0;
  }

  private get zipCodeFromInput(): string {
    return this.form.controls.zipCode.value;
  }

  public start(): void {
    const locationFromInput = this.zipCodeFromInput;

    // cache the zipCode for later
    this.zipCodeCacheService.setZipCode(locationFromInput);

    this.router.navigate(['/shops'], { queryParams: {location: locationFromInput}});
  }

}
