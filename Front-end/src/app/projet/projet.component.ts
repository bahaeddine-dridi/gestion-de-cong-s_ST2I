import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { StorageService } from '../services/storage.service';

import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-projet',

  standalone: true,
  templateUrl: './projet.component.html',
  styleUrls: ['./projet.component.css'],
  imports: [FormsModule, CommonModule, RouterModule],
})
export class ProjetComponent implements OnInit {
  projets: any[] = [];
  showNewProjetForm = false;
  newProjet = {
    name: '',
    chefId: '',
    users: '',  // Will store as string and convert to array when submitting
  };

  userRole: string = 'employee';
  isEditing = false;
  editingProjetId: number | null = null;
  constructor(
    private http: HttpClient,

    private storageService: StorageService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.fetchProjets();
    this.userRole = this.authService.getUserRole();
  }

  fetchProjets(): void {
    const token = this.storageService.getToken();
    this.http
      .get<any[]>('http://localhost:8080/api/projets', {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: (response) => {
          this.projets = response;
        },
        error: (error) => {
          console.error('Failed to fetch projets', error);
        },
      });
  }

  toggleNewProjetForm(): void {
    this.showNewProjetForm = !this.showNewProjetForm;
    if (!this.showNewProjetForm) {
      // Reset form when closing
      this.newProjet = {
        name: '',
        chefId: '',
        users: '',
      };
    }
  }

 /* onSubmit(): void {
    const token = this.storageService.getToken();

    // Convert comma-separated string to array of numbers
    const userIds = this.newProjet.users
      .split(',')
      .map(id => parseInt(id.trim()))
      .filter(id => !isNaN(id));

    const projetRequest = {
      name: this.newProjet.name,
      chefId: parseInt(this.newProjet.chefId),
      users: userIds
    };

    this.http
      .post('http://localhost:8080/api/projets', projetRequest, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: (response) => {
          console.log('Projet created:', response);
          this.fetchProjets();
          this.showNewProjetForm = false;
          this.newProjet = { name: '', chefId: '', users: '' };
        },
        error: (error) => {
          console.error('Failed to create projet', error);
        },
      });
  }
*/
onSubmit(): void {
  const token = this.storageService.getToken();
  const userIds = this.newProjet.users
    .split(',')
    .map(id => parseInt(id.trim()))
    .filter(id => !isNaN(id));

  const projetRequest = {
    name: this.newProjet.name,
    chefId: parseInt(this.newProjet.chefId),
    users: userIds
  };

  if (this.isEditing) {
    this.http.put(`http://localhost:8080/api/projets/${this.editingProjetId}`, projetRequest, {
      headers: { Authorization: `Bearer ${token}` },
    }).subscribe({
      next: (response) => {
        console.log('Projet updated:', response);
        this.fetchProjets();
        this.showNewProjetForm = false;
        this.newProjet = { name: '', chefId: '', users: '' };
        this.isEditing = false;
        this.editingProjetId = null;
      },
      error: (error) => {
        console.error('Failed to update projet', error);
      },
    });
  } else {
    this.http.post('http://localhost:8080/api/projets', projetRequest, {
      headers: { Authorization: `Bearer ${token}` },
    }).subscribe({
      next: (response) => {
        console.log('Projet created:', response);
        this.fetchProjets();
        this.showNewProjetForm = false;
        this.newProjet = { name: '', chefId: '', users: '' };
      },
      error: (error) => {
        console.error('Failed to create projet', error);
      },
    });
  }
}
  deleteProjet(projetId: number): void {
    const token = this.storageService.getToken();
    this.http.delete(`http://localhost:8080/api/projets/${projetId}`, {
      headers: { Authorization: `Bearer ${token}` },
    }).subscribe({
      next: () => {
        console.log('Projet deleted');
        this.fetchProjets(); // Rafraîchir la liste des projets
      },
      error: (error) => {
        console.error('Failed to delete projet', error);
      },
    });
  }
  editProjet(projet: any): void {
    this.newProjet = {
      name: projet.name,
      chefId: projet.chef.id.toString(),
      users: projet.users.map((user: any) => user.id).join(','),
    };
    this.showNewProjetForm = true;
    // Vous pouvez ajouter un indicateur pour savoir si vous êtes en mode édition
    this.isEditing = true;
    this.editingProjetId = projet.id;
  }

}