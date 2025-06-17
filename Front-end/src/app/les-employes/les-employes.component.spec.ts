import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LesEmployesComponent } from './les-employes.component';

describe('LesEmployesComponent', () => {
  let component: LesEmployesComponent;
  let fixture: ComponentFixture<LesEmployesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LesEmployesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LesEmployesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
