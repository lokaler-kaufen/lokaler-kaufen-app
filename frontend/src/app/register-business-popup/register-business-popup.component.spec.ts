import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterBusinessPopupComponent } from './register-business-popup.component';

describe('RegisterBusinessPopupComponent', () => {
  let component: RegisterBusinessPopupComponent;
  let fixture: ComponentFixture<RegisterBusinessPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisterBusinessPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterBusinessPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
