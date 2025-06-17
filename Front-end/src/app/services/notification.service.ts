import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders  } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from './storage.service';
const NOTIFICATION_API = 'http://localhost:8080/api/notifications';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private http: HttpClient,private storageService: StorageService) {}

  getNotifications(): Observable<any> {
    const token = this.storageService.getToken(); // Récupérez le token
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}` // Ajoutez le token dans les en-têtes
    });
    return this.http.get(NOTIFICATION_API, { headers });
  }
}