import {wrapRequest} from './api-utilities';
import {Observable, of, throwError} from 'rxjs';
import {fakeAsync} from '@angular/core/testing';

describe('API Utilities', () => {

  describe('wrapRequest', () => {

    let observable$: Observable<string>;
    const errorMessage = 'I am error.';

    it('should return a promise that resolves to the response body on success', fakeAsync(async () => {
      observable$ = of('hello, world');
      const body = await wrapRequest(observable$, errorMessage);
      expect(body).toBe('hello, world');
    }));

    it('should return a promise that rejects with the response error', fakeAsync(async () => {
      observable$ = throwError('kaput');
      try {
        await wrapRequest(observable$, errorMessage);
        fail('Should have returned a rejecting promise');
      } catch (e) {
        expect(e).toBe('kaput');
      }
    }));

  });

});
