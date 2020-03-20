import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopSearchPageComponent } from './shop-search-page.component';

describe('ShopSearchPageComponent', () => {
  let component: ShopSearchPageComponent;
  let fixture: ComponentFixture<ShopSearchPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShopSearchPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopSearchPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
