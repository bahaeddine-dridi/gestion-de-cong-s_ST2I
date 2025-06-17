import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service'; // Updated path
import { StorageService } from '../services/storage.service'; // Updated path

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService, // Injected AuthService
    private storageService: StorageService // Injected StorageService
  ) {
    // Update the form group to use 'username' instead of 'email'
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]], // Username is required
      password: ['', [Validators.required]], // Password is required
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;

      // Call the login method from AuthService
      this.authService.login(username, password).subscribe({
        next: (response) => {
          // Save token and user in session storage
          this.storageService.saveToken(response.token);
          this.storageService.saveUser(response.userInfo);


          const userRole = response.userInfo.roles.includes('ROLE_ADMIN') ? 'director' : 'employee';
          this.authService.setUserRole(userRole);
          // Redirect based on user role
          if (response.userInfo.roles.includes('ROLE_ADMIN')) {
            this.router.navigate(['/directeur']); // Admin dashboard
          } else {
            this.router.navigate(['/demandeur']); // User dashboard
          }
        },
        error: (error) => {
          console.error('Login failed', error);
          alert('Login failed. Please check your credentials.');
        },
      });
    }
  }
}