import { Routes } from '@angular/router';
import { Login } from './login/login'; // Assuming your login component is named Login
import { Landing } from './landing/landing'; // Import the new landing component

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'landing', component: Landing },
    // Redirect unauthenticated paths to login
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' } // Fallback
];

