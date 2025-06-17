import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../services/notification.service';
import { AuthService } from '../services/auth.service'; 
@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {
  userRole: string = 'employee';  // Valeur par défaut
  notifications: any[] = [];

  constructor(
    private authService: AuthService,
    private notificationService: NotificationService
  ) {}

  ngOnInit() {
    this.userRole = this.authService.getUserRole();
    this.loadNotifications();
  }

  loadNotifications() {
    this.notificationService.getNotifications().subscribe({
      next: (data) => {
        this.notifications = data;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des notifications', error);
        console.error('Détails de l\'erreur :', error.message, error.status, error.error);
      }
    });
  }
  
}