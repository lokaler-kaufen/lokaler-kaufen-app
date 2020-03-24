import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LocationControllerService, LocationSuggestionDto, LocationSuggestionsDto} from '../data/client';
import {debounceTime, map, switchMap} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {
  location: string;
  suggestions: LocationSuggestionDto[] = [];
  form: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private locationControllerService: LocationControllerService,
              private client: HttpClient) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      zipCode: ['', [Validators.required, Validators.pattern(new RegExp(/^\d{5}$/))]]
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

}
