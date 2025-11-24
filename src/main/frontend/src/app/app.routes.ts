import { Routes } from '@angular/router';
import { Login } from './feature/login/login';
import { Landing } from './feature/landing/landing';
import { Ideapage } from './feature/idea-page/ideapage';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'landing', component: Landing },
    { path: 'ideapage', component: Ideapage },
    // Redirect unauthenticated paths to login
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' } // Fallback
];

