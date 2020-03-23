import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LocationControllerService, LocationSuggestionDto} from "../data/client";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

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
              private locationControllerService: LocationControllerService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      zipCode: ['', [Validators.required, Validators.pattern(new RegExp(/^\d{5}$/))]]
    });
  }

  doSuggest(): void {
    if (!this.location) {
      return;
    }

    this.suggestions = this.locationControllerService.getSuggestionsUsingGET(this.location)
      .pipe(map((response) => response.locationSuggestions));
  }

  filter(values) {
    console.log(values);
  }
}
