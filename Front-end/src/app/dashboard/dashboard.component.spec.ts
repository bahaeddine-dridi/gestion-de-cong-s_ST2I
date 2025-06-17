import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { DashboardComponent } from './dashboard.component';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DashboardComponent,
        ReactiveFormsModule,
        RouterTestingModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Profile Form', () => {
    it('should initialize with user data', () => {
      expect(component.profileForm.get('name')?.value).toBe('employé');
      expect(component.profileForm.get('email')?.value).toBe('employé@st2i.com.t');
    });

    it('should toggle profile form visibility', () => {
      component.toggleProfileForm();
      expect(component.showProfileForm).toBeTruthy();
      
      component.toggleProfileForm();
      expect(component.showProfileForm).toBeFalsy();
    });

    it('should validate required fields', () => {
      const nameControl = component.profileForm.get('name');
      nameControl?.setValue('');
      expect(nameControl?.valid).toBeFalsy();
      
      nameControl?.setValue('employé');
      expect(nameControl?.valid).toBeTruthy();
    });
  });

  describe('Password Form', () => {
    it('should validate password match', () => {
      component.passwordForm.patchValue({
        currentPassword: 'current123',
        newPassword: 'new123456',
        confirmPassword: 'different123'
      });
      expect(component.passwordForm.hasError('mismatch')).toBeTruthy();

      component.passwordForm.patchValue({
        confirmPassword: 'new123456'
      });
      expect(component.passwordForm.hasError('mismatch')).toBeFalsy();
    });

    it('should reset password form on toggle', () => {
      component.passwordForm.patchValue({
        currentPassword: 'test123',
        newPassword: 'new123',
        confirmPassword: 'new123'
      });
      
      component.togglePasswordForm();
      component.togglePasswordForm();
      
      expect(component.passwordForm.get('currentPassword')?.value).toBe('');
      expect(component.passwordForm.get('newPassword')?.value).toBe('');
      expect(component.passwordForm.get('confirmPassword')?.value).toBe('');
    });
  });

  describe('Form Submissions', () => {
    it('should handle profile update', () => {
      spyOn(console, 'log');
      component.profileForm.patchValue({
        name: 'Jane Doe',
        email: 'jane.doe@st2i.com',
        role: 'Project Manager'
      });
      
      component.updateProfile();
      expect(console.log).toHaveBeenCalledWith('Profile update:', component.profileForm.value);
      expect(component.showProfileForm).toBeFalsy();
    });

    it('should handle password change', () => {
      spyOn(console, 'log');
      component.passwordForm.patchValue({
        currentPassword: 'current123',
        newPassword: 'new123456',
        confirmPassword: 'new123456'
      });
      
      component.changePassword();
      expect(console.log).toHaveBeenCalledWith('Password change:', component.passwordForm.value);
      expect(component.showPasswordForm).toBeFalsy();
    });
  });
});