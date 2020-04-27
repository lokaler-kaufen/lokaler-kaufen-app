import {async, ComponentFixture, fakeAsync, flushMicrotasks, TestBed, tick} from '@angular/core/testing';
import {AdminOverviewPageComponent} from './admin-overview-page.component';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AdminService} from '../service/admin.service';
import {SimpleNotificationsModule} from 'angular2-notifications';
import {By} from '@angular/platform-browser';
import {MatTableModule} from '@angular/material/table';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {Router} from '@angular/router';

describe('AdminOverviewPageComponent', () => {
  let component: AdminOverviewPageComponent;
  let fixture: ComponentFixture<AdminOverviewPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AdminOverviewPageComponent],
      imports: [
        MatTableModule,
        MatProgressSpinnerModule,
        HttpClientTestingModule,
        SimpleNotificationsModule.forRoot(),
        RouterTestingModule.withRoutes(
          [{path: 'admin/8a3bfd86-7cda-40dd-9f3e-c743154da582', component: AdminOverviewPageComponent}]
        )],
      providers: [AdminService]
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

  it('should load all shops', fakeAsync(() => {
    prepareExampleResult();
    flushMicrotasks();

    expect(fixture.debugElement.queryAll(By.css('mat-row')).length).toEqual(3);
  }));


  it('should navigate to details page on a click', fakeAsync(() => {
    prepareExampleResult();
    flushMicrotasks();

    fixture.debugElement.query(By.css('mat-row')).nativeElement.click();

    // this is necessary to wait for router changes
    tick(1);

    const routerTest = TestBed.inject(Router);
    expect(routerTest.url).toEqual('/admin/8a3bfd86-7cda-40dd-9f3e-c743154da582');
  }));

  function prepareExampleResult() {
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('/api/admin/shop');
    req.flush({
      shops: [
        {
          id: '8a3bfd86-7cda-40dd-9f3e-c743154da582',
          name: 'Moe\'s Whiskey',
          ownerName: 'Moe',
          email: 'moe@localhost',
          street: 'Lothstr. 64',
          zipCode: '85579',
          city: 'Neubiberg',
          addressSupplement: '',
          contactTypes: [
            'WHATSAPP',
            'FACETIME'
          ],
          enabled: true,
          approved: true,
          details: 'Bester Whiskey in ganz Neubiberg!',
          website: 'https://www.moes-whiskey.com/',
          slots: {
            timePerSlot: 15,
            timeBetweenSlots: 5,
            monday: {
              start: '08:00',
              end: '17:00'
            },
            tuesday: {
              start: '08:00',
              end: '17:00'
            },
            wednesday: {
              start: '08:00',
              end: '17:00'
            },
            thursday: {
              start: '08:00',
              end: '17:00'
            },
            friday: {
              start: '08:00',
              end: '17:00'
            },
            saturday: {
              start: '08:00',
              end: '17:00'
            },
            sunday: {
              start: '08:00',
              end: '17:00'
            }
          }
        },
        {
          id: '17396f5b-b5ae-4e86-b4b4-d594310dfb79',
          name: 'Flo\'s Kaffeeladen',
          ownerName: 'Flo',
          email: 'flo@localhost',
          street: 'Aschauer Str. 32',
          zipCode: '81549',
          city: 'München',
          addressSupplement: '',
          contactTypes: [
            'GOOGLE_DUO'
          ],
          enabled: true,
          approved: true,
          details: '',
          website: null,
          slots: {
            timePerSlot: 30,
            timeBetweenSlots: 10,
            monday: null,
            tuesday: null,
            wednesday: null,
            thursday: null,
            friday: null,
            saturday: null,
            sunday: null
          }
        },
        {
          id: '855809ab-d239-4229-876f-000ac9d658e7',
          name: 'Vroni\'s Kleiderladen',
          ownerName: 'Vroni',
          email: 'vroni@localhost',
          street: 'Rheinstraße 4C',
          zipCode: '55116',
          city: 'Mainz',
          addressSupplement: '',
          contactTypes: [
            'GOOGLE_DUO',
            'SIGNAL'
          ],
          enabled: true,
          approved: true,
          details: '',
          website: null,
          slots: {
            timePerSlot: 60,
            timeBetweenSlots: 15,
            monday: null,
            tuesday: null,
            wednesday: null,
            thursday: null,
            friday: null,
            saturday: null,
            sunday: null
          }
        }
      ]
    });
  }
});
