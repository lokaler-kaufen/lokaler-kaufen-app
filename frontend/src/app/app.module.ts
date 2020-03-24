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
import {HttpClientModule} from '@angular/common/http';
import {MatSortModule} from '@angular/material/sort';
import {BookingPopupComponent} from './booking-popup/booking-popup.component';
import {MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule} from '@angular/material/dialog';
import {ErrorStateMatcher, ShowOnDirtyErrorStateMatcher} from '@angular/material/core';
import {RegisterBusinessPopupComponent} from './register-business-popup/register-business-popup.component';
import {MatStepperModule} from '@angular/material/stepper';
import {ApiModule} from './data/client';
import {AdminOverviewPageComponent} from './admin-overview/admin-overview-page.component';
import {ShopCreationSuccessPopupComponent} from './shop-creation-success-popup/shop-creation-success-popup.component';
import {AdminLoginPageComponent} from './admin-login/admin-login-page.component';
import {MatPasswordStrengthModule} from '@angular-material-extensions/password-strength';
import {SimpleNotificationsModule} from "angular2-notifications";
import {LoginFormComponent} from './login-form/login-form.component';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {PasswordResetPopupComponent} from './password-reset-popup/password-reset-popup.component';

@NgModule({
  declarations: [
    AdminOverviewPageComponent,
    AppComponent,
    LandingPageComponent,
    ShopSearchPageComponent,
    ShopDetailsPageComponent,
    ShopCreationPageComponent,
    ShopManagementPageComponent,
    LoginPageComponent,
    PrivacyPageComponent,
    ImprintPageComponent,
    AppHeaderComponent,
    AppFooterComponent,
    BookingPopupComponent,
    RegisterBusinessPopupComponent,
    AdminOverviewPageComponent,
    ShopCreationSuccessPopupComponent,
    AdminOverviewPageComponent,
    AdminLoginPageComponent,
    LoginFormComponent,
    PasswordResetPopupComponent
  ],
  imports: [
    ApiModule,
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    FormsModule,
    MatTableModule,
    HttpClientModule,
    MatIconModule,
    FormsModule,
    MatInputModule,
    MatSelectModule,
    MatCardModule,
    MatTableModule,
    MatSortModule,
    HttpClientModule,
    MatStepperModule,
    MatPasswordStrengthModule,
    SimpleNotificationsModule.forRoot(),
    MatAutocompleteModule,
  ],
  providers: [
    {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}},
    {provide: ErrorStateMatcher, useClass: ShowOnDirtyErrorStateMatcher}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
