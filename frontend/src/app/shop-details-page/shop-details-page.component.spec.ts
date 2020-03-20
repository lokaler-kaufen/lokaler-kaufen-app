import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopDetailsPageComponent } from './shop-details-page.component';

describe('ShopDetailsPageComponent', () => {
  let component: ShopDetailsPageComponent;
  let fixture: ComponentFixture<ShopDetailsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShopDetailsPageComponent ]
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
});
