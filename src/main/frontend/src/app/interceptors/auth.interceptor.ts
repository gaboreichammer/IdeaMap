import {
  HttpEvent,
  HttpRequest,
  HttpInterceptorFn, // Used to define the function type
  HttpHandlerFn      // Used for the 'next' parameter in functional interceptors
} from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

/**
 * Interceptor that automatically attaches the JWT token to the Authorization header
 * for all outgoing requests.
 */
export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {

  // Inject the AuthService to get the token
  const authService = inject(AuthService);

  // 1. Get the token from the service (which reads from localStorage)
  const authToken = authService.getToken();

  // 2. Check if a token exists
  if (authToken) {
    // 3. Clone the request and add the Authorization header
    // The Bearer scheme is the standard way to send JWTs.
    const clonedRequest = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${authToken}`)
    });

    // 4. Pass the cloned request to the next handler
    return next(clonedRequest);
  }

  // 5. If no token, pass the original request without modification
  return next(req);
};
