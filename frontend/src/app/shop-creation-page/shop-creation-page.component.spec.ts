import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopCreationPageComponent } from './shop-creation-page.component';

describe('ShopCreationPageComponent', () => {
  let component: ShopCreationPageComponent;
  let fixture: ComponentFixture<ShopCreationPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShopCreationPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopCreationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
