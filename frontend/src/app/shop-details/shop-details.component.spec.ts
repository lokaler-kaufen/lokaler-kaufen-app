import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ShopDetailsComponent} from './shop-details.component';

describe('ShopDetailsComponent', () => {
  let component: ShopDetailsComponent;
  let fixture: ComponentFixture<ShopDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShopDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
