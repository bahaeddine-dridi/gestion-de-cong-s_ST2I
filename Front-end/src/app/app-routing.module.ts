import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { DemandeurComponent } from './demandeur/demandeur.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DirecteurComponent } from './directeur/directeur.component';
import { ProjetComponent } from './projet/projet.component';
import { DemandeComponent } from './demande/demande.component'; // Import DemandeComponent
import { NotificationComponent } from './notification/notification.component';
import { LesEmployesComponent } from './les-employes/les-employes.component';
import { DemandeCongeComponent } from './demande-conge/demande-conge.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' }, // Default route (redirects to login)
  { path: 'login', component: LoginComponent }, // Login page
  { path: 'demandeur', component: DemandeurComponent }, // Demandeur page
  { path: 'dashboard', component: DashboardComponent }, // Dashboard page
  { path: 'directeur', component: DirecteurComponent }, // Directeur page
  { path: 'projet', component: ProjetComponent }, // Projet page
  { path: 'demande', component: DemandeComponent }, // Demande page
  { path: 'LesEmployes', component:LesEmployesComponent, }, 
  { path: 'DemandeConge', component:DemandeCongeComponent, }, 
  { path: 'notification', component:NotificationComponent, }, 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}