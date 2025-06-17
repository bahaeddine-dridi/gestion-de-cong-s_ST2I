import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { StorageService } from '../services/storage.service';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

interface LeaveBalance {
  id: number;
  userId: number;
  congeRepos: number;
  congeMaladie: number;
  initialDate: string;
}

interface DemandStats {
  accepted: number;
  rejected: number;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userRole: string = '';
  leaveBalance: LeaveBalance = { 
    id: 0, 
    userId: 0, 
    congeRepos: 0, 
    congeMaladie: 0, 
    initialDate: '' 
  };
  demandStats: DemandStats = { accepted: 0, rejected: 0 };

  constructor(
    private http: HttpClient,
    private storageService: StorageService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.userRole = this.authService.getUserRole() || 'employee';
    this.fetchLeaveBalance();
    this.fetchDemandStatistics();
  }

  private getToken(): string | null {
    return this.storageService.getToken();
  }

  private fetchLeaveBalance() {
    const token = this.getToken();
    if (!token) {
      console.error('No token found. User must be authenticated.');
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    // Updated to use the new endpoint
    this.http.get<LeaveBalance>('http://localhost:8080/api/solde-conge/my', { headers })
      .pipe(
        catchError(error => {
          console.error('Error fetching leave balance:', error);
          if (error.status === 401) {
            console.error('Unauthorized - Invalid or expired JWT token.');
            // You might want to handle token expiration here
            // this.authService.logout(); // If you have a logout method
          }
          return throwError(() => new Error('Error fetching leave balance'));
        })
      )
      .subscribe({
        next: (response) => {
          this.leaveBalance = response;
        },
        error: (error) => {
          console.error('Failed to fetch leave balance:', error);
          // Handle error appropriately (show user message, etc.)
        }
      });
  }

  private fetchDemandStatistics() {
    const token = this.getToken();
    if (!token) {
      console.error('No token found. User must be authenticated.');
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    this.http.get<DemandStats>(`http://localhost:8080/api/demandes/user/stats`, { headers })
      .pipe(
        catchError(error => {
          console.error('Error fetching demand statistics:', error);
          if (error.status === 401) {
            console.error('Unauthorized - Invalid or expired JWT token.');
          }
          return throwError(() => new Error('Error fetching demand statistics'));
        })
      )
      .subscribe({
        next: (response) => {
          this.demandStats = response;
        },
        error: (error) => {
          console.error('Failed to fetch demand statistics:', error);
        }
      });
  }

  // Methods for the circular progress indicators
  getProgressValue(value: number): string {
    const circumference = 2 * Math.PI * 45;
    const maxValue = 30; // Maximum days
    const progress = (value / maxValue) * circumference;
    return `${progress} ${circumference}`;
  }

  getColorClass(value: number): string {
    if (value === 0) return 'progress-low';
    if (value > 15) return 'progress-high';
    return 'progress-medium';
  }
}