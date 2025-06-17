import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-directeur',
  templateUrl: './directeur.component.html',
  styleUrl: './directeur.component.css'
})
export class DirecteurComponent {
profileForm: FormGroup;
  passwordForm: FormGroup;
  showProfileForm = false;
  showPasswordForm = false;
  user = {
    name: 'Directeur Géneral',
    email: 'employé@st2i.com',
    role: 'Project Manager'
  };

  constructor(private fb: FormBuilder) {
    this.profileForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      role: ['']
    });

    this.passwordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  ngOnInit() {
    this.profileForm.patchValue(this.user);
  }

  passwordMatchValidator(g: FormGroup) {
    return g.get('newPassword')?.value === g.get('confirmPassword')?.value
      ? null : { mismatch: true };
  }

  updateProfile() {
    if (this.profileForm.valid) {
      console.log('Profile update:', this.profileForm.value);
      // Implement profile update logic here
      this.showProfileForm = false;
    }
  }

  changePassword() {
    if (this.passwordForm.valid) {
      console.log('Password change:', this.passwordForm.value);
      // Implement password change logic here
      this.showPasswordForm = false;
      this.passwordForm.reset();
    }
  }

  toggleProfileForm() {
    this.showProfileForm = !this.showProfileForm;
    if (!this.showProfileForm) {
      this.profileForm.reset(this.user);
    }
  }

  togglePasswordForm() {
    this.showPasswordForm = !this.showPasswordForm;
    if (!this.showPasswordForm) {
      this.passwordForm.reset();
    }
  }
}
