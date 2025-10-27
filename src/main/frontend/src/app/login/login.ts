import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, finalize, throwError } from 'rxjs';
import { AuthService, LoginCredentials } from '../services/auth.service'; // Assuming these are exported from auth.service

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  private authService = inject(AuthService);

  // State using Angular Signals for reactivity
  username = signal('');
  password = signal('');
  message = signal('');
  error = signal('');
  isLoading = signal(false);
  isSuccess = signal(false); // To control message box styling

  /**
   * Handles the form submission, calls the AuthService, and processes the result.
   */
  login(): void {
    // 1. Reset status
    this.message.set('');
    this.error.set('');
    this.isSuccess.set(false);
    this.isLoading.set(true);

    const credentials: LoginCredentials = {
      username: this.username(),
      password: this.password()
    };

    // 2. Call the service and handle success/error
    this.authService.login(credentials)
      .pipe(
        finalize(() => this.isLoading.set(false)), // Stop loading regardless of result
        // Catch network errors or 4xx/5xx status codes
        catchError((err: HttpErrorResponse) => {
          // If 401 Unauthorized, the error body (err.error) contains the message
          if (err.status === 401 && typeof err.error === 'string') {
            // This captures "Invalid username or password." from your Spring controller
            this.error.set(err.error);
          } else {
            // General failure message
            this.error.set('Connection error. Please check the server status. ' + err.error);
          }
          this.isSuccess.set(false);
          // Re-throw the error to keep the observable pipe correct
          return throwError(() => err);
        })
      )
      .subscribe({
        // 3. Handle successful response (HTTP 200)
        next: (response: string) => {
          // This captures "Login successful!" from your Spring controller
          this.message.set(response);
          this.isSuccess.set(true);
        },
        // Error is primarily handled in the catchError pipe, but included for completeness
        error: () => {
          // Do nothing, error signal is already set in catchError
        }
      });
  }
}
