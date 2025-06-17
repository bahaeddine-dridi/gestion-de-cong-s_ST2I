import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { StorageService } from '../services/storage.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { User } from '../user.model';

interface SoldeCongeForm {
  userId: number;
  congeRepos: number;
  congeMaladie: number;
}

@Component({
  selector: 'app-les-employes',
  standalone: true,
  templateUrl: './les-employes.component.html',
  styleUrls: ['./les-employes.component.css'],
  imports: [FormsModule, CommonModule, RouterModule],
})
export class LesEmployesComponent implements OnInit {
  userRole: string = '';
  users: User[] = [];
  showNewUserForm: boolean = false;
  showUpdateUserForm: boolean = false;
  showSoldeForm: boolean = false;
  newUser: User = new User(0, '', '', '', [{ id: 0, name: 'ROLE_USER' }]);
  isLoading: boolean = false;

  soldeForm: SoldeCongeForm = {
    userId: 0,
    congeRepos: 0,
    congeMaladie: 0,
  };

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.userRole = this.authService.getUserRole();
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.isLoading = true;
    const token = this.storageService.getToken();
    this.http
      .get<User[]>('http://localhost:8080/api/users', {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: (response) => {
          this.users = response;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Failed to fetch users', error);
          this.isLoading = false;
        },
      });
  }

  toggleNewUserForm(): void {
    this.showNewUserForm = !this.showNewUserForm;
    this.showUpdateUserForm = false;
    this.showSoldeForm = false;
  }

  toggleUpdateUserForm(): void {
    this.showUpdateUserForm = !this.showUpdateUserForm;
    this.showNewUserForm = false;
    this.showSoldeForm = false;
  }

  openSoldeForm(userId: number): void {
    this.soldeForm.userId = userId;
    this.showSoldeForm = true;
  }

  closeSoldeForm(): void {
    this.showSoldeForm = false;
    this.soldeForm = {
      userId: 0,
      congeRepos: 0,
      congeMaladie: 0,
    };
  }

  createUserSolde(): void {
    this.isLoading = true;
    const token = this.storageService.getToken();
    this.http
      .post('http://localhost:8080/api/solde-conge', this.soldeForm, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      })
      .subscribe({
        next: (response) => {
          alert('Solde congé créé avec succès');
          this.closeSoldeForm();
          this.isLoading = false;
        },
        error: (error) => {
          alert('Erreur lors de la création du solde congé');
          console.error('Failed to create solde congé', error);
          this.isLoading = false;
        },
      });
  }

  addUser(): void {
    this.isLoading = true;
    const token = this.storageService.getToken();
    this.http
      .post<User>('http://localhost:8080/api/users', this.newUser, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: (response) => {
          this.users.push(response);
          this.showNewUserForm = false;
          this.newUser = new User(0, '', '', '', [{ id: 0, name: 'ROLE_USER' }]);
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Failed to add user', error);
          this.isLoading = false;
        },
      });
  }

  editUser(user: User): void {
    this.newUser = { ...user };
    this.showUpdateUserForm = true;
  }

  updateUser(): void {
    this.isLoading = true;
    const token = this.storageService.getToken();
    this.http
      .put<User>(`http://localhost:8080/api/users/${this.newUser.id}`, this.newUser, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: (response) => {
          const index = this.users.findIndex((user) => user.id === this.newUser.id);
          if (index !== -1) {
            this.users[index] = response;
          }
          this.showUpdateUserForm = false;
          this.newUser = new User(0, '', '', '', [{ id: 0, name: 'ROLE_USER' }]);
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Failed to update user', error);
          this.isLoading = false;
        },
      });
  }

  deleteUser(userId: number): void {
    const token = this.storageService.getToken();
    this.http
      .delete(`http://localhost:8080/api/users/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: () => {
          this.users = this.users.filter((user) => user.id !== userId);
        },
        error: (error) => {
          console.error('Failed to delete user', error);
        },
      });
  }
}