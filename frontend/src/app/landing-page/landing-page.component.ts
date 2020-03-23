import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LocationControllerService, LocationSuggestionDto} from "../data/client";
import {debounceTime, map, switchMap} from "rxjs/operators";
import {Observable} from "rxjs";

@Component({
  selector: 'landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {
  location: string;
  suggestions: Observable<LocationSuggestionDto[]>;
  form: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private locationControllerService: LocationControllerService) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      zipCode: ['', [Validators.required, Validators.pattern(new RegExp(/^\d{5}$/))]]
    });
    this.suggestions = this.form.controls.zipCode.valueChanges
      .pipe(
        debounceTime(100),
        switchMap(value => {
          if (value) {
            return this.locationControllerService.getSuggestionsUsingGET(value)
              .pipe(map((response) => response.locationSuggestions));
          } else {
            return [];
          }
        }));
  }

  filter(values) {
    console.log(values);
  }
}
