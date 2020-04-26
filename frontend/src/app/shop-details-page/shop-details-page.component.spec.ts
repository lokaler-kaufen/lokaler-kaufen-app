import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ShopDetailsPageComponent} from './shop-details-page.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {MatDialogModule} from '@angular/material/dialog';
import {SimpleNotificationsModule} from 'angular2-notifications';
import {TranslateModule} from '@ngx-translate/core';

describe('ShopDetailsPageComponent', () => {
  let component: ShopDetailsPageComponent;
  let fixture: ComponentFixture<ShopDetailsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        MatDialogModule,
        SimpleNotificationsModule.forRoot(),
        TranslateModule.forRoot()
      ],
      declarations: [ShopDetailsPageComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate back when method back is called', () => {
    spyOn(window.history, 'back');

    component.back();

    expect(window.history.back).toHaveBeenCalled();
  });
});
