import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AppFooterComponent} from './app-footer.component';
import {UserContextService} from '../shared/user-context.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('AppFooterComponent', () => {
  let component: AppFooterComponent;
  let fixture: ComponentFixture<AppFooterComponent>;
  let userContextService: UserContextService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      declarations: [AppFooterComponent]
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

});
