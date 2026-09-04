import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FraudApiService } from '../../services/fraud-api.service';

@Component({
  selector: 'app-fraud-graph',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './fraud-graph.html',
  styleUrl: './fraud-graph.css'
})
export class FraudGraphComponent implements OnInit {
  accounts: any[] = [];
  transactions: any[] = [];
  errorMessage: string = '';

  // Variables de Paginación y Filtrado
  currentPage: number = 1;
  pageSize: number = 10;
  filterOnlyFraud: boolean = false;

  constructor(
    private fraudApi: FraudApiService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.fraudApi.getAccounts().subscribe({
      next: (data) => {
        this.accounts = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.errorMessage = 'Error al cargar las cuentas del backend.';
        console.error(err);
        this.cdr.markForCheck();
      }
    });

    this.fraudApi.getTransactions().subscribe({
      next: (data) => {
        this.transactions = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.errorMessage = 'Error al cargar las transacciones del backend.';
        console.error(err);
        this.cdr.markForCheck();
      }
    });
  }

  // Getter para calcular cuántas transacciones son fraude en total
  get fraudCount(): number {
    return this.transactions.filter(tx => tx.isFraud === true).length;
  }

  // Obtiene la lista filtrada (todas o solo las de fraude)
  get filteredTransactions(): any[] {
    if (this.filterOnlyFraud) {
      return this.transactions.filter(tx => tx.isFraud === true);
    }
    return this.transactions;
  }

  // Alterna el filtro al hacer clic en la tarjeta superior
  toggleFraudFilter(): void {
    this.filterOnlyFraud = !this.filterOnlyFraud;
    this.currentPage = 1; // Reinicia a la primera página al filtrar
  }

  // Getters y métodos de paginación basados en la lista filtrada
  get totalPages(): number {
    const total = Math.ceil(this.filteredTransactions.length / this.pageSize);
    return total > 0 ? total : 1;
  }

  get paginatedTransactions(): any[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.filteredTransactions.slice(startIndex, startIndex + this.pageSize);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }
}
