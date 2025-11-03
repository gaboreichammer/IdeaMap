import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

// The Idea structure mirrors the backend Idea model fields
export interface Idea {
  id: string;
  name: string;
  text: string;
  link: string;
  image: string;
  linkedIdeaIds: string[];
  tags: Tag[];
  userId: string;
  linkedIdeas: Idea[];
}

export interface Tag {
  id: string;
  name: string;
}

export interface IdeaLink {
  id: string;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class IdeaService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  private readonly ideaBaseUrl = `${environment.apiBaseUrl}/idea`;

  /**
   * Helper to retrieve authenticated headers (same as IdeaGroupService)
   */
  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : new HttpHeaders();
  }

  /**
   * Fetches a specific Idea by its ID.
   * @param ideaId The ID of the Idea to retrieve.
   * @returns An Observable of the Idea object.
   */
  getIdeaById(ideaId: string): Observable<Idea> {
    const url = `${this.ideaBaseUrl}/linked/${ideaId}`;
    return this.http.get<Idea>(url, { headers: this.getAuthHeaders() });
  }
}
