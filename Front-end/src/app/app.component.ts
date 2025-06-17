import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from './services/auth.service';
import { StorageService } from './services/storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  userName: string = '';
  userEmail: string = '';
  title = 'gestion_conge_stage2';
  isNotificationPage = false;
  isDropdownVisible = false;
 
  constructor(
    private router: Router,
    private authService: AuthService,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.loadUserData();
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.isNotificationPage = this.router.url === '/login';
        // Recharger les données utilisateur à chaque changement de route
        this.loadUserData();
      }
    });
  }

  loadUserData() {
    const user = this.storageService.getUser();
    if (user) {
      this.userName = user.username || 'N/A';
      this.userEmail = user.email || 'N/A';
    }
  }

  // Toggle dropdown
  toggleDropdown(): void {
    this.isDropdownVisible = !this.isDropdownVisible;
  }

  // Logout method
  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.storageService.clear();
        this.userName = '';
        this.userEmail = '';
        this.router.navigate(['/login']);
      },
      error: (error: any) => {
        console.error('Logout failed', error);
        alert('Logout failed. Please try again.');
      },
    });
  }
}
