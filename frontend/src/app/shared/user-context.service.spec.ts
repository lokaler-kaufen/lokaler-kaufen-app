import { TestBed } from '@angular/core/testing';

import { UserContextService } from './user-context.service';

describe('UserContextService', () => {
  let service: UserContextService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserContextService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should intially return false', () => {
    expect(service.isLoggedInStoreOwner).toBeFalsy();
  });

  it('should return true if storeOwnerLoggedIn was called', () => {
    service.storeOwnerLoggedIn();

    expect(service.isLoggedInStoreOwner).toBeTruthy();
  });

});
