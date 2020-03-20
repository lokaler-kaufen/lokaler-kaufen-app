import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { ShopSearchPageComponent } from './shop-search-page/shop-search-page.component';
import { ShopDetailsPageComponent } from './shop-details-page/shop-details-page.component';
import { ShopCreationPageComponent } from './shop-creation-page/shop-creation-page.component';
import { ShopManagementPageComponent } from './shop-management-page/shop-management-page.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { PrivacyPageComponent } from './privacy-page/privacy-page.component';
import { ImprintPageComponent } from './imprint-page/imprint-page.component';
import { AppHeaderComponent } from './app-header/app-header.component';
import { AppFooterComponent } from './app-footer/app-footer.component';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@NgModule({
  declarations: [
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
    AppFooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
