import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LandingPageComponent} from './landing-page/landing-page.component';
import {ShopSearchPageComponent} from './shop-search-page/shop-search-page.component';
import {ShopDetailsPageComponent} from './shop-details-page/shop-details-page.component';
import {ShopCreationPageComponent} from './shop-creation-page/shop-creation-page.component';
import {ShopManagementPageComponent} from './shop-management-page/shop-management-page.component';
import {LoginPageComponent} from './login-page/login-page.component';
import {ImprintPageComponent} from './imprint-page/imprint-page.component';
import {PrivacyPageComponent} from './privacy-page/privacy-page.component';
import {AdminOverviewPageComponent} from './admin-overview/admin-overview-page.component';
import {AdminLoginPageComponent} from './admin-login/admin-login-page.component';
import {PasswordResetPageComponent} from './password-reset-page/password-reset-page.component';
import {CancelReservationComponent} from './cancel-reservation/cancel-reservation.component';
import {AdminDetailsPageComponent} from './admin-details-page/admin-details-page.component';
import {NormalLayoutComponent} from './layouts/normal-layout/normal-layout.component';
import {AdminLayoutComponent} from './layouts/admin-layout/admin-layout.component';
import {LandingLayoutComponent} from './layouts/landing-layout/landing-layout.component';

const routes: Routes = [
  {
    path: '', component: LandingLayoutComponent, children: [
      {path: '', component: LandingPageComponent},
    ]
  },
  {
    path: '', component: NormalLayoutComponent, children: [
      {path: 'shops', component: ShopSearchPageComponent},
      {path: 'shops/:id', component: ShopDetailsPageComponent},
      {path: 'create-shop', component: ShopCreationPageComponent},
      {path: 'manage-shop', component: ShopManagementPageComponent},
      {path: 'login', component: LoginPageComponent},
      {path: 'imprint', component: ImprintPageComponent},
      {path: 'privacy', component: PrivacyPageComponent},
      {path: 'reset-shop-password', component: PasswordResetPageComponent},
      {path: 'cancel-reservation', component: CancelReservationComponent}
    ]
  },
  {
    path: 'admin', component: AdminLayoutComponent, children: [
      {path: '', component: AdminLoginPageComponent},
      {path: 'overview', component: AdminOverviewPageComponent},
      {path: ':id', component: AdminDetailsPageComponent},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
