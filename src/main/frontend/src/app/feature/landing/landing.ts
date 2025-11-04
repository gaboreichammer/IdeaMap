import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { IdeaGroupService, IdeaGroup } from '../../services/idea-group.service';
import { IdeaService, Idea } from '../../services/idea.service';
import { Ideapage } from '../idea-page/ideapage';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, Ideapage],
  templateUrl: './landing.html',
  styleUrl: './landing.css'
})
export class Landing implements OnInit {
  private authService = inject(AuthService);
  private ideaGroupService = inject(IdeaGroupService)
  private ideaService = inject(IdeaService);
  private router = inject(Router);

  username = signal<string>('');
  ideaGroups = signal<IdeaGroup[]>([]);

  // State for the currently selected Idea Group
  selectedIdeaGroup = signal<IdeaGroup | null>(null);
  // State for the Idea linked to the selected Idea Group
  linkedIdea = signal<Idea | null>(null);
  isLoadingLinkedIdea = signal<boolean>(false);

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

            // Optionally select the first item on load
            if (groups.length > 0) {
                this.selectIdeaGroup(groups[0]);
            }
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
   * Handles selecting an IdeaGroup from the list and fetches its linked Idea.
   * @param group The IdeaGroup selected by the user.
   */
  selectIdeaGroup(group: IdeaGroup): void {
    // 1. Update the selected group signal
    this.selectedIdeaGroup.set(group);
    this.linkedIdea.set(null); // Clear previous idea

    const linkedId = group.linkedIdeaId;

    if (linkedId) {
        this.isLoadingLinkedIdea.set(true);
        // 2. Fetch the linked Idea using the dedicated service and ID
        this.ideaService.getIdeaById(linkedId).subscribe({
            next: (idea) => {
                this.linkedIdea.set(idea);
                this.isLoadingLinkedIdea.set(false);
            },
            error: (err) => {
                console.error(`Failed to load linked Idea ${linkedId}:`, err);
                this.linkedIdea.set(null);
                this.isLoadingLinkedIdea.set(false);
            }
        });
    }
  }

  onIdeaLinkClick(ideaId: string) {
    console.log(`Landingpage received new idea request for ID: ${ideaId}`);
    this.isLoadingLinkedIdea.set(true);
            // 2. Fetch the linked Idea using the dedicated service and ID
            this.ideaService.getIdeaById(ideaId).subscribe({
                next: (idea) => {
                    this.linkedIdea.set(idea);
                    this.isLoadingLinkedIdea.set(false);
                },
                error: (err) => {
                    console.error(`Failed to load linked Idea ${ideaId}:`, err);
                    this.linkedIdea.set(null);
                    this.isLoadingLinkedIdea.set(false);
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
