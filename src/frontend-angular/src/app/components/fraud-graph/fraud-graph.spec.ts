import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, it, expect, beforeEach } from 'vitest';
import { provideRouter } from '@angular/router';
import { FraudGraphComponent } from './fraud-graph';

describe('FraudGraphComponent', () => {
  let component: FraudGraphComponent;
  let fixture: ComponentFixture<FraudGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FraudGraphComponent],
      providers: [provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(FraudGraphComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});