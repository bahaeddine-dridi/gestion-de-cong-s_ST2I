import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { StorageService } from '../services/storage.service';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { CommonModule } from '@angular/common'; // Import CommonModule for DatePipe
import { RouterModule } from '@angular/router'; 
import { AuthService } from '../services/auth.service';
@Component({
  selector: 'app-demande',
  standalone: true, // Ensure this is true
  templateUrl: './demande.component.html',
  styleUrls: ['./demande.component.css'],
  imports: [FormsModule, CommonModule,RouterModule], // Add CommonModule here
})
export class DemandeComponent implements OnInit {
  demandes: any[] = [];
  showNewDemandeForm = false;
  newDemande = {
    type: '', // Will store the selected type (e.g., "Congés_principale")
    dateDebut: '', // Will store the date in ISO format
    dateFin: '', // Will store the date in ISO format
  };

  // List of available types (matching the backend enum)
  types = [
    'Congés_principale',
    'Congés_de_maladie',
    'Congés_maternité',
    'Congé_sans_solde',
  ];

  constructor(private http: HttpClient, private storageService: StorageService,private authService: AuthService) {}
  userRole: string = 'employee';  // Valeur par défaut

  
  ngOnInit(): void {
    this.fetchUserDemandes();
// Récupérer le rôle de l'utilisateur
this.userRole = this.authService.getUserRole();
  }

  // Fetch user demandes from the backend
  fetchUserDemandes(): void {
    const token = this.storageService.getToken();
    this.http
      .get<any[]>('http://localhost:8080/api/demandes/my', {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: (response) => {
          // Convert date strings to Date objects
          this.demandes = response.map(demande => ({
            ...demande,
            date_debut: new Date(demande.date_debut), // Convert to Date object
            date_fin: new Date(demande.date_fin), // Convert to Date object
          }));
        },
        error: (error) => {
          console.error('Failed to fetch demandes', error);
        },
      });
  }

  // Toggle the new demande form
  toggleNewDemandeForm(): void {
    this.showNewDemandeForm = !this.showNewDemandeForm;
  }

  // Submit a new demande
  onSubmit(): void {
    const token = this.storageService.getToken();

    // Prepare the request body
    const demandeRequest = {
      type: this.newDemande.type, // Use the selected type
      date_debut: new Date(this.newDemande.dateDebut).toISOString(), // Convert to ISO format
      date_fin: new Date(this.newDemande.dateFin).toISOString(), // Convert to ISO format
    };

    // Send the POST request
    this.http
      .post('http://localhost:8080/api/demandes', demandeRequest, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: (response) => {
          console.log('Demande created:', response);
          this.fetchUserDemandes(); // Refresh the list of demandes
          this.showNewDemandeForm = false; // Hide the form
          this.newDemande = { type: '', dateDebut: '', dateFin: '' }; // Reset the form
        },
        error: (error) => {
          console.error('Failed to create demande', error);
        },
      });
  }




}