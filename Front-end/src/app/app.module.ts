import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';

// Components
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DemandeurComponent } from './demandeur/demandeur.component';
import { DirecteurComponent } from './directeur/directeur.component';
import { NotificationComponent } from './notification/notification.component';

// Import standalone components correctly
import { ProjetComponent } from './projet/projet.component';
import { DemandeCongeComponent } from './demande-conge/demande-conge.component';
import { LesEmployesComponent } from './les-employes/les-employes.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    DemandeurComponent,
    DirecteurComponent,
    NotificationComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    ProjetComponent, // ✅ Import standalone component
    DemandeCongeComponent, // ✅ Import standalone component
    LesEmployesComponent, // ✅ Import standalone component
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
