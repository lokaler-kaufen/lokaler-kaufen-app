import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ContactTypesComponent} from './contact-types.component';

describe('ContactTypesComponent', () => {
  let component: ContactTypesComponent;
  let fixture: ComponentFixture<ContactTypesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContactTypesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactTypesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
