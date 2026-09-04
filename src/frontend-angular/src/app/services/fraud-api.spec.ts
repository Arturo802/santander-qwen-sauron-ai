import { TestBed } from '@angular/core/testing';
import { describe, it, expect, beforeEach } from 'vitest';
import { FraudApiService } from './fraud-api.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('FraudApiService', () => {
  let service: FraudApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FraudApiService]
    });
    service = TestBed.inject(FraudApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});