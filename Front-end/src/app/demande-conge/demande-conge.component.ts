import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { StorageService } from '../services/storage.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-demande-conge',
  standalone: true,
  templateUrl: './demande-conge.component.html',
  styleUrls: ['./demande-conge.component.css'],
  imports: [CommonModule, RouterModule],
})
export class DemandeCongeComponent implements OnInit {
  pendingDemandes: any[] = [];
  decidedDemandes: any[] = [];
  showDetailModal = false;
  selectedDemande: any = null;
  userRole: string = 'employee';

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.fetchDemandes();
    this.userRole = this.authService.getUserRole();
  }

  // Get headers in the exact format that works with the API
  private getHeaders(): HttpHeaders {
    const token = this.storageService.getToken();
    return new HttpHeaders({
      'accept': '*/*',
      'Authorization': `Bearer ${token}`
    });
  }

  fetchDemandes(): void {
    this.http
      .get<any[]>('http://localhost:8080/api/projets/chef/demandes', {
        headers: this.getHeaders()
      })
      .subscribe({
        next: (response) => {
          const processedDemandes = response.map(demande => ({
            ...demande,
            date_debut: new Date(demande.date_debut),
            date_fin: new Date(demande.date_fin),
            duration: this.calculateDuration(new Date(demande.date_debut), new Date(demande.date_fin))
          }));

          this.pendingDemandes = processedDemandes.filter(d => d.statut === 'en_attente');
          this.decidedDemandes = processedDemandes.filter(d => d.statut !== 'en_attente');
        },
        error: (error) => {
          console.error('Failed to fetch demandes', error);
        },
      });
  }

  calculateDuration(dateDebut: Date, dateFin: Date): string {
    const start = new Date(dateDebut);
    const end = new Date(dateFin);
    
    if (start.toDateString() === end.toDateString()) {
      const diffMs = end.getTime() - start.getTime();
      const diffHrs = Math.floor(diffMs / (1000 * 60 * 60));
      const diffMins = Math.round((diffMs % (1000 * 60 * 60)) / (1000 * 60));
      
      if (diffHrs === 0) {
        return `${diffMins} minutes`;
      } else if (diffMins === 0) {
        return `${diffHrs} heures`;
      } else {
        return `${diffHrs} heures, ${diffMins} minutes`;
      }
    } else {
      const diffTime = Math.abs(end.getTime() - start.getTime());
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      return diffDays === 1 ? '1 jour' : `${diffDays} jours`;
    }
  }

  calculateDurationInDays(dateDebut: Date, dateFin: Date): number {
    const diffTime = Math.abs(new Date(dateFin).getTime() - new Date(dateDebut).getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  updateSoldeConge(userId: number, daysConsumed: number): void {
    // Using the exact format from the working curl example
    this.http
      .post(
        `http://localhost:8080/api/solde-conge/${userId}/consume?daysConsumed=${daysConsumed}`,
        '',  // empty body as in curl example
        {
          headers: this.getHeaders(),
          responseType: 'text'
        }
      )
      .subscribe({
        next: (response) => {
          console.log('Leave balance updated successfully:', response);
        },
        error: (error) => {
          console.error('Failed to update leave balance', error);
        }
      });
  }

  updateSickLeaveConge(userId: number, daysConsumed: number): void {
    // Using the exact format from the working curl example
    this.http
      .post(
        `http://localhost:8080/api/solde-conge/${userId}/consume-sick-leave?daysConsumed=${daysConsumed}`,
        '',  // empty body as in curl example
        {
          headers: this.getHeaders(),
          responseType: 'text'
        }
      )
      .subscribe({
        next: (response) => {
          console.log('Sick leave balance updated successfully:', response);
        },
        error: (error) => {
          console.error('Failed to update sick leave balance', error);
        }
      });
  }

  openDetailModal(demande: any): void {
    this.selectedDemande = demande;
    this.showDetailModal = true;
  }

  closeDetailModal(): void {
    this.showDetailModal = false;
    this.selectedDemande = null;
  }

  formatDate(date: Date): string {
    return date.toLocaleDateString('fr-FR');
  }

  sendNotification(userToId: number, dateDebut: Date, dateFin: Date, status: string): void {
    const currentUser = this.storageService.getUser();
    
    const notification = {
      content: `votre demande de congé du ${this.formatDate(dateDebut)} au ${this.formatDate(dateFin)} est ${status}`,
      userToId: userToId,
      userFromId: currentUser.id
    };

    this.http
      .post('http://localhost:8080/api/notifications', notification, {
        headers: this.getHeaders(),
      })
      .subscribe({
        next: (response) => {
          console.log('Notification sent successfully:', response);
        },
        error: (error) => {
          console.error('Failed to send notification', error);
        }
      });
  }

  updateDemandeStatus(demandeId: number, newStatus: 'aprouvé' | 'rejetté'): void {
    this.http
      .put(
        `http://localhost:8080/api/demandes/${demandeId}/statut?statut=${newStatus}`,
        null,
        {
          headers: this.getHeaders()
        }
      )
      .subscribe({
        next: (response) => {
          console.log('Status updated successfully:', response);

          if (this.selectedDemande && newStatus === 'aprouvé') {
            const daysConsumed = this.calculateDurationInDays(
              this.selectedDemande.date_debut,
              this.selectedDemande.date_fin
            );

            // Updated condition to match your API types
            if (this.selectedDemande.type === 'Congés_principale') {
              // Deduct from congeRepos (regular leave)
              this.updateSoldeConge(this.selectedDemande.user.id, daysConsumed);
            } else if (this.selectedDemande.type === 'Congés_de_maladie') {
              // Deduct from congeMaladie (sick leave)
              this.updateSickLeaveConge(this.selectedDemande.user.id, daysConsumed);
            }

            // Send notification
            this.sendNotification(
              this.selectedDemande.user.id,
              this.selectedDemande.date_debut,
              this.selectedDemande.date_fin,
              'acceptée'
            );
          } else if (this.selectedDemande) {
            // Send rejection notification
            this.sendNotification(
              this.selectedDemande.user.id,
              this.selectedDemande.date_debut,
              this.selectedDemande.date_fin,
              'refusée'
            );
          }

          this.fetchDemandes();
          this.closeDetailModal();
        },
        error: (error) => {
          console.error('Failed to update status', error);
        },
      });
  }
}