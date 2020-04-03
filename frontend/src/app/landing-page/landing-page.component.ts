import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {debounceTime, map, switchMap} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {ZipCodeCacheService} from './zip-code-cache.service';
import {NotificationsService} from 'angular2-notifications';
import {LocationSuggestionDto, LocationSuggestionsDto} from '../data/api';
import {UserContextService} from '../shared/user-context.service';
import {RegisterBusinessPopupComponent} from '../register-business-popup/register-business-popup.component';
import {MatDialog} from '@angular/material/dialog';
import {BreakpointObserver} from '@angular/cdk/layout';

@Component({
  selector: 'landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {

  /**
   * Reference to the shop registration dialog.
   */
  private dialogRef;

  private zipCodeInitialValue: string;

  suggestions: LocationSuggestionDto[] = [];

  form: FormGroup;

  isSmallScreen: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private client: HttpClient,
    private router: Router,
    private zipCodeCacheService: ZipCodeCacheService,
    private notificationsService: NotificationsService,
    private userContextService: UserContextService,
    private dialog: MatDialog,
    breakpointObserver: BreakpointObserver
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
        switchMap(value => {
          if (value) {
            return this.client.get<LocationSuggestionsDto>('/api/location/suggestion?zipCode=' + value)
              .pipe(map(({suggestions}) => suggestions));
          } else {
            return [];
          }
        })).subscribe(suggestions => this.suggestions = suggestions);
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

  get isLoggedInShopOwner(): boolean {
    return this.userContextService.isLoggedInStoreOwner;
  }

  openRegisterBusinessPopup(): void {
    if (!this.dialogRef) {
      this.dialogRef = this.dialog.open(RegisterBusinessPopupComponent, {
        width: '500px'
      });

      this.dialogRef.afterClosed().subscribe(
        () => {
          // Reset the dialogRef because the user should be able to open the dialog again.
          this.dialogRef = null;
        }
      );
    }
  }

  start(): void {
    const locationFromInput = this.zipCodeFromInput;

    // cache the zipCode for later
    this.zipCodeCacheService.setZipCode(locationFromInput);

    this.client.get('/api/location?zipCode=' + encodeURIComponent(locationFromInput)).subscribe(() => {
        this.router.navigate(['/shops'], {queryParams: {location: locationFromInput}});
      },
      error => {
        if (error.status === 404) {
          this.notificationsService.error('Ungültige PLZ', 'Diese Postleitzahl kennen wir leider nicht, haben Sie sich vertippt?');
        } else {
          this.notificationsService.error('Tut uns Leid!', 'Wir können diese Postleitzahl gerade nicht verarbeiten.');
        }
      });

  }

  private get zipCodeFromInput(): string {
    return this.form.controls.zipCode.value;
  }

  logoutStoreOwner() {
    this.client.delete('/api/shop/login', {}).toPromise()
      .then(() => {
        console.log('Shop Owner logged out. ');
        this.userContextService.storeOwnerLoggedOut();
        this.router.navigate(['/login']);
      })
      .catch(error => {
        console.log('Unable to logout shop owner: ' + error.status + ' ' + error.message);
        this.notificationsService.error('Tut uns Leid!', 'Beim Logout ist etwas schiefgegangen.');
      });
  }

}
