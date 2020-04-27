import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

/**
 * Takes the given response observable and turns it into a easier-to-digest promise.
 *
 * If an error is caught in the observable, the given "errorMessage" string will be used to log it to the console. The
 * actual error will be re-thrown, i.e. the returned promise will be rejected with it. If no error occur, the returned
 * promise resolves to the response body.
 *
 * This method is not intended as gotta-get-em-all solution but rather aims to reduce boilerplate code for the classic
 * 80% of requests where you don't need to do anything fancy.
 *
 * @param response$ Observable as received from any request made with the default HttpClient.
 * @param errorMessage Message to prepend the logged error.
 *
 * @return A promise resolving to the response body on success or rejecting to the error.
 */
export function wrapRequest<T>(response$: Observable<any>, errorMessage: string): Promise<T> {
  return response$
    .pipe(
      catchError(error => {
        console.error(errorMessage, error.body);
        return throwError(error);
      })
    )
    .toPromise();
}
