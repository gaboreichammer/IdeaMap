import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Interface for the login credentials payload.
 * Matches the structure of the LoginRequest DTO in Java.
 */
export interface LoginCredentials {
  username: string;
  password: string;
}

@Injectable({
  // Use 'providedIn: 'root'' for best practice, making it a singleton service.
  providedIn: 'root'
})
export class AuthService {
  // Inject HttpClient using the 'inject' function (Angular 14+)
  private http = inject(HttpClient);

  // The base URL for the Spring Boot application (change if different)
  private baseUrl = 'http://localhost:8080/api/users';

  /**
   * Sends a POST request to the Spring Boot /login endpoint.
   *
   * @param credentials The username and password.
   * @returns An Observable of type string, which will contain the success message
   * or the error message if the request fails (401 Unauthorized).
   */
  login(credentials: LoginCredentials): Observable<string> {
    const url = `${this.baseUrl}/login`;

    // The POST request expects LoginCredentials as the body, and the response
    // is expected to be a string (the success/failure message).
    // The Spring Boot controller returns 'ResponseEntity<String>', which corresponds
    // to a string body in Angular.
    return this.http.post(url, credentials, {
      responseType: 'text' // Important: tells HttpClient to expect a string response, not JSON
    });
  }

}
