import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopManagementPageComponent } from './shop-management-page.component';

describe('ShopManagementPageComponent', () => {
  let component: ShopManagementPageComponent;
  let fixture: ComponentFixture<ShopManagementPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShopManagementPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopManagementPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
