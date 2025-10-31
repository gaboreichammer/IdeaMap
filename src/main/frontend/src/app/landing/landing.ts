import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { IdeaGroupService, IdeaGroup } from '../services/idea-group.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './landing.html',
  styleUrl: './landing.css'
})
export class Landing implements OnInit {
  private authService = inject(AuthService);
  private ideaGroupService = inject(IdeaGroupService)
  private router = inject(Router);

  // State to hold the displayed username
  username = signal<string>('');
  // State to hold the fetched list of Idea Groups
  ideaGroups = signal<IdeaGroup[]>([]);

  ngOnInit(): void {
    // Attempt to retrieve and display the username upon loading
    const user = this.authService.getLoggedInUsername();
    if (user) {
      this.username.set(user);
      this.fetchIdeaGroups();
    } else {
      // If no valid token/user info, redirect to login
      this.router.navigate(['/login']);
    }
  }

  /**
   * Calls the service to fetch Idea Groups for the authenticated user.
   */
  fetchIdeaGroups(): void {
    this.ideaGroupService.getIdeaGroupsForCurrentUser().subscribe({
        next: (groups) => {
            console.log('Idea Groups loaded:', groups);
            this.ideaGroups.set(groups);
        },
        error: (err) => {
            console.error('Failed to load Idea Groups:', err);
            // If the request fails, especially with 401/403, we should log out.
            if (err.status === 401 || err.status === 403) {
                this.logout();
            }
        }
    });
  }

  /**
   * Clears the token and navigates the user back to the login screen.
   */
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
