import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppFooterComponent } from './app-footer.component';
import {MatDialogModule} from '@angular/material/dialog';
import {UserContextService} from '../shared/user-context.service';
import {By} from '@angular/platform-browser';

describe('AppFooterComponent', () => {
  let component: AppFooterComponent;
  let fixture: ComponentFixture<AppFooterComponent>;
  let userContextService: UserContextService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ MatDialogModule ],
      declarations: [ AppFooterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    userContextService = TestBed.inject(UserContextService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should display the login for non logged in users', () => {
    const links = fixture.debugElement.queryAll(By.css('a'));

    expect(links[0].nativeElement.textContent).toEqual('Login für Ladenbesitzer');
    expect(links[1].nativeElement.textContent).toEqual('Als Ladenbesitzer registrieren');
    expect(links[2].nativeElement.textContent).toEqual('Impressum');
  });

  it('Should not display login or registration for loggedin users but edit instead', () => {
    userContextService.storeOwnerLoggedIn();
    fixture.detectChanges();

    const links = fixture.debugElement.queryAll(By.css('a'));

    expect(links[0].nativeElement.textContent).toEqual('Laden bearbeiten');
    expect(links[1].nativeElement.textContent).toEqual('Impressum');
  });
});
