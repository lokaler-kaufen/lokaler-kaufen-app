import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminOverviewPageComponent } from './admin-overview-page.component';

describe('AdminOverviewComponent', () => {
  let component: AdminOverviewPageComponent;
  let fixture: ComponentFixture<AdminOverviewPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminOverviewPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminOverviewPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
