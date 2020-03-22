import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ShopCreationSuccessPopupComponent} from './shop-creation-success-popup.component';

describe('ShopCreationSuccessPopupComponent', () => {
  let component: ShopCreationSuccessPopupComponent;
  let fixture: ComponentFixture<ShopCreationSuccessPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShopCreationSuccessPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopCreationSuccessPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
