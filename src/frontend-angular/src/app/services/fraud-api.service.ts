import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, shareReplay } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FraudApiService {
  private apiUrl = 'http://localhost:8080/api/ingest';

  // Cachés en memoria para evitar llamadas repetidas al regresar entre vistas
  private accounts$?: Observable<any>;
  private transactions$?: Observable<any>;

  constructor(private http: HttpClient) {}

  getAccounts(): Observable<any> {
    if (!this.accounts$) {
      this.accounts$ = this.http.get(`${this.apiUrl}/accounts`).pipe(
        shareReplay(1) // Almacena la última respuesta en caché
      );
    }
    return this.accounts$;
  }

  getTransactions(): Observable<any> {
    if (!this.transactions$) {
      this.transactions$ = this.http.get(`${this.apiUrl}/transactions`).pipe(
        shareReplay(1) // Almacena la última respuesta en caché
      );
    }
    return this.transactions$;
  }

  // Método opcional por si se necesita forzar una recarga limpia al re-ingresar datos
  clearCache(): void {
    this.accounts$ = undefined;
    this.transactions$ = undefined;
  }
}
