import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlotsComponent } from './slots.component';

describe('SlotsComponent', () => {
  let component: SlotsComponent;
  let fixture: ComponentFixture<SlotsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SlotsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlotsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
