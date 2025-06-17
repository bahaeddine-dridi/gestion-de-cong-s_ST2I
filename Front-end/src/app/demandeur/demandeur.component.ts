import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-demandeur',
  templateUrl: './demandeur.component.html',
  styleUrl: './demandeur.component.css'
  
})
export class DemandeurComponent {
 
  user = {
    name: 'employé',
    email: 'employé@st2i.com',
    role: 'Project Manager'
  };

 

  ngOnInit() {
    
  }

}
