import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';
import {LandingPageComponent} from './landing-page/landing-page.component';
import {ShopSearchPageComponent} from './shop-search-page/shop-search-page.component';
import {ShopDetailsPageComponent} from './shop-details-page/shop-details-page.component';
import {ShopCreationPageComponent} from './shop-creation-page/shop-creation-page.component';
import {ShopManagementPageComponent} from './shop-management-page/shop-management-page.component';
import {LoginPageComponent} from './login-page/login-page.component';
import {PrivacyPageComponent} from './privacy-page/privacy-page.component';
import {ImprintPageComponent} from './imprint-page/imprint-page.component';
import {AppHeaderComponent} from './app-header/app-header.component';
import {AppFooterComponent} from './app-footer/app-footer.component';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatTableModule} from '@angular/material/table';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {MatSortModule} from '@angular/material/sort';
import {BookingPopupComponent} from './booking-popup/booking-popup.component';
import {MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule} from '@angular/material/dialog';
import {ErrorStateMatcher, ShowOnDirtyErrorStateMatcher} from '@angular/material/core';
import {RegisterBusinessPopupComponent} from './register-business-popup/register-business-popup.component';
import {MatStepperModule} from '@angular/material/stepper';
import {AdminOverviewPageComponent} from './admin-overview/admin-overview-page.component';
import {ShopCreationSuccessPopupComponent} from './shop-creation-success-popup/shop-creation-success-popup.component';
import {AdminLoginPageComponent} from './admin-login/admin-login-page.component';
import {MatPasswordStrengthModule} from '@angular-material-extensions/password-strength';
import {SimpleNotificationsModule} from 'angular2-notifications';
import {LoginFormComponent} from './login-form/login-form.component';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {PasswordResetPopupComponent} from './password-reset-popup/password-reset-popup.component';
import {PasswordResetPageComponent} from './password-reset-page/password-reset-page.component';
import {CancelReservationComponent} from './cancel-reservation/cancel-reservation.component';
import {AdminDetailsPageComponent} from './admin-details-page/admin-details-page.component';
import {ShopDetailsConfigComponent} from './shop-details-config/shop-details-config.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {ContactTypesComponent} from './contact-types/contact-types.component';
import {MatDividerModule} from '@angular/material/divider';
import {NormalLayoutComponent} from './layouts/normal-layout/normal-layout.component';
import {LandingLayoutComponent} from './layouts/landing-layout/landing-layout.component';
import {AdminLayoutComponent} from './layouts/admin-layout/admin-layout.component';
import {MatTabsModule} from '@angular/material/tabs';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatGridListModule} from '@angular/material/grid-list';
import {PageFooterComponent} from './page-footer/page-footer.component';
import {ShopLogoComponent} from './shop-logo/shop-logo.component';
import {LogoutInterceptor} from './data/logout.interceptor';
import {SlotsComponent} from './slots/slots.component';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {BookingSuccessPopupComponent} from './booking-success-popup/booking-success-popup.component';

// AoT requires an exported function for factories
export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AdminDetailsPageComponent,
    AdminLayoutComponent,
    AdminLoginPageComponent,
    AdminOverviewPageComponent,
    AppComponent,
    AppFooterComponent,
    AppHeaderComponent,
    BookingPopupComponent,
    BookingSuccessPopupComponent,
    CancelReservationComponent,
    ContactTypesComponent,
    ImprintPageComponent,
    LandingLayoutComponent,
    LandingPageComponent,
    LoginFormComponent,
    LoginPageComponent,
    NormalLayoutComponent,
    PageFooterComponent,
    PasswordResetPageComponent,
    PasswordResetPopupComponent,
    PrivacyPageComponent,
    RegisterBusinessPopupComponent,
    ShopCreationPageComponent,
    ShopCreationSuccessPopupComponent,
    ShopDetailsConfigComponent,
    ShopDetailsPageComponent,
    ShopLogoComponent,
    ShopManagementPageComponent,
    ShopSearchPageComponent,
    SlotsComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    MatAutocompleteModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatPasswordStrengthModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatSortModule,
    MatStepperModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    ReactiveFormsModule,
    SimpleNotificationsModule.forRoot(),
    TranslateModule.forRoot({
      defaultLanguage: 'de',
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: LogoutInterceptor, multi: true},
    {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}},
    {provide: ErrorStateMatcher, useClass: ShowOnDirtyErrorStateMatcher}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
