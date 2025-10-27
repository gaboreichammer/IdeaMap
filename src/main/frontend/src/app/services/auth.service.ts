import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface LoginResponse {
  message: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  // Inject PLATFORM_ID to determine the current execution context (server or browser)
  private platformId = inject(PLATFORM_ID);

  // Use a flag to avoid repetitive checks in methods
  private isBrowser = isPlatformBrowser(this.platformId);

  private baseUrl = 'http://localhost:8080/api/users';
  private readonly TOKEN_KEY = 'auth_token';

  /**
   * Sends a POST request to the Spring Boot /login endpoint.
   *
   * @param credentials The username and password.
   * @returns An Observable of type LoginResponse containing the message and JWT token.
   */
  login(credentials: LoginCredentials): Observable<LoginResponse> {
    const url = `${this.baseUrl}/login`;
    return this.http.post<LoginResponse>(url, credentials);
  }

  storeToken(token: string): void {
    // Check if running in the browser before accessing localStorage
    if (this.isBrowser) {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  getToken(): string | null {
    // Check if running in the browser before accessing localStorage
    if (this.isBrowser) {
      return localStorage.getItem(this.TOKEN_KEY);
    }
    return null; // Return null if executed on the server
  }

  logout(): void {
    // Check if running in the browser before accessing localStorage
    if (this.isBrowser) {
      localStorage.removeItem(this.TOKEN_KEY);
    }
  }

  /**
   * Decodes the JWT and extracts the 'sub' (subject/username) claim.
   * NOTE: This is client-side decoding and should only be used for UI display,
   * never for security checks.
   * @returns The username string or null if the token is invalid or missing.
   */
  getLoggedInUsername(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }

    try {
      // JWTs are base64 encoded strings split by dots: Header.Payload.Signature
      const payload = token.split('.')[1];
      // Decode the payload from Base64
      const decodedPayload = atob(payload);
      const claims = JSON.parse(decodedPayload);

      // Assuming your JwtService in Spring uses 'sub' (subject) for the username
      return claims.sub || null;
    } catch (e) {
      console.error('Error decoding JWT:', e);
      return null;
    }
  }
}
