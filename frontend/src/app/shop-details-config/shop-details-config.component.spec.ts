import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ShopDetailsConfigComponent} from './shop-details-config.component';

describe('ShopDetailsComponent', () => {
  let component: ShopDetailsConfigComponent;
  let fixture: ComponentFixture<ShopDetailsConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShopDetailsConfigComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopDetailsConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
