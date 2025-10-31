import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

// Define the structure of the IdeaGroup entity returned from the backend
export interface IdeaGroup {
  id: string; // The ObjectId, serialized as a String
  name: string;
  linkedIdeaId: string;
  userId: string; // The ObjectId, serialized as a String
}

@Injectable({
  providedIn: 'root'
})
export class IdeaGroupService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  // Base URL for the IdeaGroup controller
  private readonly baseUrl = 'http://localhost:8080/api/idea';

  /**
   * Retrieves the current JWT token from AuthService and constructs the Authorization header.
   * @returns HttpHeaders containing the Bearer token, or an empty header if no token is found.
   */
  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();

    if (token) {
      // Best Practice: JWT must be prefixed with 'Bearer ' for Spring Security Resource Server
      return new HttpHeaders().set('Authorization', `Bearer ${token}`);
    }

    // Return empty headers if no token exists. HttpClient will handle the 401 error.
    return new HttpHeaders();
  }

  /**
   * Fetches the list of IdeaGroups for the currently authenticated user.
   * * Since the Spring Boot endpoint now securely extracts the userId from the JWT,
   * this GET request sends the JWT in the header and requires no body or query parameters.
   *
   * @returns An Observable of an array of IdeaGroup objects.
   */
  getIdeaGroupsForCurrentUser(): Observable<IdeaGroup[]> {
    const url = `${this.baseUrl}/getIdeaGroup`;

    return this.http.get<IdeaGroup[]>(url, { headers: this.getAuthHeaders() });
  }

  // --- Best Practice Recommendations ---

  // 1. Error Handling: You would typically pipe the Observable and add catchError here.
  // 2. Refresh Token: If you implement token refreshing, that logic would reside in a
  //    higher-order service or interceptor that handles 401/403 responses.
}
