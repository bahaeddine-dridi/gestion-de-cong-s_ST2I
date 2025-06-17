import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:8080/api/auth/';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUser: any = null; // Stocker les détails de l'utilisateur localement

  constructor(private http: HttpClient) {}

  // Login method
  login(username: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'signin', { username, password });
  }

  // Logout method
  logout(): Observable<any> {
    localStorage.removeItem('userRole');
    localStorage.removeItem('userData'); // Supprimer les détails de l'utilisateur
    this.currentUser = null;
    return this.http.post(AUTH_API + 'signout', {}); // Call the logout API
  }

  // Set the user role after login
  setUserRole(role: string): void {
    localStorage.setItem('userRole', role);
  }

  // Get the user role
  getUserRole(): string {
    return localStorage.getItem('userRole') || 'employee';
  }

  // Set user data after login
  setUserData(user: any): void {
    this.currentUser = user; 
    localStorage.setItem('userData', JSON.stringify(user)); // Stocker dans localStorage
  }

  // Get user data
  getUserData(): any {
    if (!this.currentUser) {
      const userData = localStorage.getItem('userData');
      this.currentUser = userData ? JSON.parse(userData) : null;
    }
    return this.currentUser;
  }
  getUserId(): number | null {
    const userData = this.getUserData();
    return userData ? userData.id : null;
  }
  
}
