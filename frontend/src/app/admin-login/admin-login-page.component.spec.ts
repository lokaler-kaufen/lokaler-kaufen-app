import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminLoginPageComponent } from './admin-login-page.component';

describe('AdminLoginComponent', () => {
  let component: AdminLoginPageComponent;
  let fixture: ComponentFixture<AdminLoginPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminLoginPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminLoginPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
