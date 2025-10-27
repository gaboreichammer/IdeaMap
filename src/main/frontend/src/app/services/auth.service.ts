import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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

  private baseUrl = 'http://localhost:8080/api/users';
  // Use a constant for the storage key to avoid typos
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

  /**
   * Stores the JWT token in localStorage.
   */
  storeToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  /**
   * Retrieves the JWT token from localStorage.
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Clears the token from storage (logout).
   */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }
}
