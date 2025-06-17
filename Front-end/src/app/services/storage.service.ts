import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  constructor() {}

  // Save token
  saveToken(token: string): void {
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  // Get token
  getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  // Save user
  saveUser(user: any): void {
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  // Get user
  getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  // Clear storage (logout)
  clear(): void {
    window.sessionStorage.clear();
  }
}