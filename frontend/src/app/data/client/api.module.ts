import {ModuleWithProviders, NgModule, Optional, SkipSelf} from '@angular/core';
import {Configuration} from './configuration';
import {HttpClient} from '@angular/common/http';


import {AdminLoginControllerService} from './api/adminLoginController.service';
import {BasicErrorControllerService} from './api/basicErrorController.service';
import {LocationControllerService} from './api/locationController.service';
import {ReservationControllerService} from './api/reservationController.service';
import {ShopAdminControllerService} from './api/shopAdminController.service';
import {ShopControllerService} from './api/shopController.service';
import {ShopLoginControllerService} from './api/shopLoginController.service';

@NgModule({
  imports:      [],
  declarations: [],
  exports:      [],
  providers: [
    AdminLoginControllerService,
    BasicErrorControllerService,
    LocationControllerService,
    ReservationControllerService,
    ShopAdminControllerService,
    ShopControllerService,
    ShopLoginControllerService ]
})
export class ApiModule {
    public static forRoot(configurationFactory: () => Configuration): ModuleWithProviders {
        return {
            ngModule: ApiModule,
            providers: [ { provide: Configuration, useFactory: configurationFactory } ]
        };
    }

    constructor( @Optional() @SkipSelf() parentModule: ApiModule,
                 @Optional() http: HttpClient) {
        if (parentModule) {
            throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
        }
        if (!http) {
            throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
            'See also https://github.com/angular/angular/issues/20575');
        }
    }
}
