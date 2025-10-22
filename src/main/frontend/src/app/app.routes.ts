import { Routes } from '@angular/router';
import { Login } from './login/login'; // Import the Login component

export const routes: Routes = [
  {
    // Path: '' corresponds to the root URL (e.g., http://localhost:4200/)
    path: '',
    component: Login,
    title: 'Login' // Optional: Sets the browser tab title
  },
  {
    // Optional: Adds a fallback for any unknown routes
    path: '**',
    redirectTo: ''
  }
];
