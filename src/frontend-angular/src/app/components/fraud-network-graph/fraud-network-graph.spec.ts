import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, it, expect, beforeEach } from 'vitest';
import { provideRouter } from '@angular/router';
import { FraudNetworkGraphComponent } from './fraud-network-graph';

describe('FraudNetworkGraphComponent', () => {
  let component: FraudNetworkGraphComponent;
  let fixture: ComponentFixture<FraudNetworkGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FraudNetworkGraphComponent],
      providers: [provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(FraudNetworkGraphComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});