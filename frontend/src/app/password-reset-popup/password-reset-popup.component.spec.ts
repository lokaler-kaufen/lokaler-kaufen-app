import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PasswordResetPopupComponent} from './password-reset-popup.component';

describe('PasswordResetPopupComponent', () => {
  let component: PasswordResetPopupComponent;
  let fixture: ComponentFixture<PasswordResetPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PasswordResetPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordResetPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
