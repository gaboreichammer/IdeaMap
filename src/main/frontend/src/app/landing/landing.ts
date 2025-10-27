import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './landing.html',
  styleUrl: './landing.css'
})
export class Landing implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);

  // State to hold the displayed username
  username = signal<string>('');

  ngOnInit(): void {
    // Attempt to retrieve and display the username upon loading
    const user = this.authService.getLoggedInUsername();
    if (user) {
      this.username.set(user);
    } else {
      // If no valid token/user info, redirect to login
      this.router.navigate(['/login']);
    }
  }

  /**
   * Clears the token and navigates the user back to the login screen.
   */
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
