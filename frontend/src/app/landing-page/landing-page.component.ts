import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {debounceTime, map, switchMap} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {ZipCodeCacheService} from './zip-code-cache.service';
import {LocationSuggestionDto} from '../data/client/model/locationSuggestionDto';
import {LocationSuggestionsDto} from '../data/client/model/locationSuggestionsDto';
import {NotificationsService} from 'angular2-notifications';

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

  constructor(
    private formBuilder: FormBuilder,
    private client: HttpClient,
    private router: Router,
    private zipCodeCacheService: ZipCodeCacheService,
    private notificationsService: NotificationsService
  ) {
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
    return this.form.valid;
  }

  private get zipCodeFromInput(): string {
    return this.form.controls.zipCode.value;
  }

  public start(): void {
    const locationFromInput = this.zipCodeFromInput;

    // cache the zipCode for later
    this.zipCodeCacheService.setZipCode(locationFromInput);

    this.client.get('/api/location?zipCode=' + encodeURIComponent(locationFromInput)).subscribe(() => {
        this.router.navigate(['/shops'], {queryParams: {location: locationFromInput}});
      },
      error => {
        if (error.status === 404) {
          this.notificationsService.error('Ungültige PLZ', 'Diese Postleitzahl kennen wir leider nicht, hast du dich vertippt?');
        } else {
          this.notificationsService.error('Tut uns Leid!', 'Wir können diese Postleitzahl gerade nicht verarbeiten.');
        }
      });

  }

}
