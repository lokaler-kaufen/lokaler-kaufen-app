export * from './adminLoginController.service';
import {AdminLoginControllerService} from './adminLoginController.service';
import {BasicErrorControllerService} from './basicErrorController.service';
import {LocationControllerService} from './locationController.service';
import {ReservationControllerService} from './reservationController.service';
import {ShopAdminControllerService} from './shopAdminController.service';
import {ShopControllerService} from './shopController.service';
import {ShopLoginControllerService} from './shopLoginController.service';

export * from './basicErrorController.service';
export * from './locationController.service';
export * from './reservationController.service';
export * from './shopAdminController.service';
export * from './shopController.service';
export * from './shopLoginController.service';
export const APIS = [AdminLoginControllerService, BasicErrorControllerService, LocationControllerService, ReservationControllerService, ShopAdminControllerService, ShopControllerService, ShopLoginControllerService];
