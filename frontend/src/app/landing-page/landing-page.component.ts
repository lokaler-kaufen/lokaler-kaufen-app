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
  zipCode: string;
  suggestions: LocationSuggestionDto[] = [];
  form: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private locationControllerService: LocationControllerService,
              private client: HttpClient,
              private router: Router,
              private zipCodeCacheService: ZipCodeCacheService) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      // pre-fill with value from cache (if present)
      zipCode: [this.zipCodeCacheService.getZipCode(), [Validators.required, Validators.pattern(new RegExp(/^\d{5}$/))]]
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

  public start(): void {
    const locationFromInput = this.form.controls.zipCode.value;

    // cache the zipCode for later
    this.zipCodeCacheService.setZipCode(locationFromInput);

    this.router.navigate(['/shops'], { queryParams: {location: locationFromInput}});
  }

}
