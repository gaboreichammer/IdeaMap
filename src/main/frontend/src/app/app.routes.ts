import { Routes } from '@angular/router';
import { Login } from './feature/login/login'; // Assuming your login component is named Login
import { Landing } from './feature/landing/landing'; // Import the new landing component
import { Ideapage } from './feature/idea-page/ideapage';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'landing', component: Landing },
    { path: 'ideapage', component: Ideapage },
    // Redirect unauthenticated paths to login
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' } // Fallback
];

