import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { IdeaGroupService, IdeaGroup } from '../../services/idea-group.service';
import { IdeaService, Idea } from '../../services/idea.service';
import { IdeaGroupNew } from '../idea-group-new/ideaGroupNew';
import { Ideapage } from '../idea-page/ideapage';
import { Observable, timer, combineLatest, of } from 'rxjs';
import { finalize, switchMap, catchError } from 'rxjs/operators';

const MIN_LOADING_TIME_MS = 500;
const MAX_BREADCRUMBS = 10;

export interface Breadcrumb {
  id: string;
  groupId: string;
  groupName: string;
  name: string;
}

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, Ideapage, IdeaGroupNew],
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
  breadcrumbs = signal<Breadcrumb[]>([]);

  // State for the currently selected Idea Group
  selectedIdeaGroup = signal<IdeaGroup | null>(null);
  // State for the Idea linked to the selected Idea Group
  linkedIdea = signal<Idea | null>(null);
  isLoadingLinkedIdea = signal<boolean>(false);
  isAddingNewIdeaGroup = signal<boolean>(false);

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
         this.selectedIdeaGroup.set(group);
         this.loadIdeaWithMinTime(group.linkedIdeaId);
     }

     /**
      * Handles the event when an idea link is clicked inside the Ideapage component.
      * @param ideaId The ID of the linked Idea to load.
      */
     onIdeaLinkClick(ideaId: string): void {
         this.loadIdeaWithMinTime(ideaId);
     }

    /**
     * Helper function to manage the loading process with a minimum display time.
     * @param ideaId The ID of the idea to load.
     */
    private loadIdeaWithMinTime(ideaId: string | null): void {
        this.isAddingNewIdeaGroup.set(false);
        this.linkedIdea.set(null); // Clear previous idea
        this.isLoadingLinkedIdea.set(true);

        if (!ideaId) {
            this.isLoadingLinkedIdea.set(false);
            return;
        }

        // 1. Define the Observable for the data fetch
        // Use catchError to ensure the Observable completes gracefully even on error
        const data$ = this.ideaService.getIdeaById(ideaId).pipe(
            catchError((err) => {
                console.error(`Failed to load linked Idea ${ideaId}:`, err);
                return of(null as Idea | null); // Emit null on error
            })
        );

        // 2. Define the Observable for the minimum time delay
        const minTime$ = timer(MIN_LOADING_TIME_MS);

        // 3. Wait for BOTH the data and the minimum time to complete
        combineLatest([data$, minTime$]).pipe(
            // The loading is finished when both observables have emitted their value
            // We only need the first value (the idea data)
            finalize(() => {
                // Ensure the loading state is turned off regardless of success/error
                this.isLoadingLinkedIdea.set(false);
            })
        ).subscribe({
            next: ([idea, _]) => {
               this.loadIdeaAndBreadcrumbs(idea);
            }
        });
    }

  clickedBreadcrumb(clickedBreadcrumb: Breadcrumb) {
    this.loadIdeaWithMinTime(clickedBreadcrumb.id);

    //Find the IdeaGroup that matches the new Idea's ideaGroupId
    const matchingGroup = this.ideaGroups().find(
        group => group.id === clickedBreadcrumb.groupId
    );

    //Update the selectedIdeaGroup signal if a match is found
    if (matchingGroup) {
      this.selectedIdeaGroup.set(matchingGroup);
     }
  }

  loadIdeaAndBreadcrumbs(idea: Idea | null) {
     // Check if idea is valid before proceeding
      if (!idea) {
        this.linkedIdea.set(null);
        return;
      }

      this.linkedIdea.set(idea);

      const matchingGroup = this.ideaGroups().find(
        group => group.id === idea.ideaGroupId
      );

      if(matchingGroup) {
      const newBreadcrumb: Breadcrumb = {
          id: idea.id,
          groupId: matchingGroup.id,
          groupName: matchingGroup.name,
          name: idea.name
        };

      this.breadcrumbs.update(currentBreadcrumbs => {
      // Filter out the current version of the new breadcrumb (if it exists)
      // This removes the duplicate while keeping the other items in order.
       let updatedBreadcrumbs = currentBreadcrumbs.filter(
         b => b.id !== newBreadcrumb.id
       );

       //Add the NEW version of the breadcrumb to the end
       updatedBreadcrumbs.push(newBreadcrumb);

        // If the new array exceeds the max size, remove the oldest (first) item
        if (updatedBreadcrumbs.length > MAX_BREADCRUMBS) {
          // .slice(1) returns a new array starting from the second element (index 1)
          updatedBreadcrumbs = updatedBreadcrumbs.slice(1);
        }

        return updatedBreadcrumbs;
      });
    }
  }

  addNewIdeaGroup() {
    this.isAddingNewIdeaGroup.set(true);
  }

  /**
   * Clears the token and navigates the user back to the login screen.
   */
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
