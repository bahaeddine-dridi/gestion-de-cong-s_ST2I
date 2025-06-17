import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DirecteurComponent } from './directeur.component';

describe('DirecteurComponent', () => {
  let component: DirecteurComponent;
  let fixture: ComponentFixture<DirecteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DirecteurComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DirecteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
