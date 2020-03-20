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

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    ShopSearchPageComponent,
    ShopDetailsPageComponent,
    ShopCreationPageComponent,
    ShopManagementPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
