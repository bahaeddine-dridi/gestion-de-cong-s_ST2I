import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandeurComponent } from './demandeur.component';

describe('DemandeurComponent', () => {
  let component: DemandeurComponent;
  let fixture: ComponentFixture<DemandeurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DemandeurComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemandeurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
