import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { StorageService } from './services/storage.service'; // Import StorageService

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private storageService: StorageService, private router: Router) {}

  canActivate(): boolean {
    // Check if the user is authenticated (has a token)
    if (this.storageService.getToken()) {
      return true; // Allow access to the route
    } else {
      // Redirect to the login page if not authenticated
      this.router.navigate(['/login']);
      return false; // Block access to the route
    }
  }
}