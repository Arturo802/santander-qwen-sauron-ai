import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FraudApiService } from '../../services/fraud-api.service';

@Component({
  selector: 'app-fraud-network-graph',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './fraud-network-graph.html',
  styleUrls: ['./fraud-network-graph.css']
})
export class FraudNetworkGraphComponent implements OnInit {
  loading: boolean = true;
  totalNodes: number = 0;
  totalEdges: number = 0;
  fraudNodesCount: number = 0;
  recentFraudConnections: any[] = [];

  constructor(private fraudApi: FraudApiService) {}

  ngOnInit(): void {
    this.processNetworkData();
  }

  processNetworkData(): void {
    this.loading = true;
    this.fraudApi.getTransactions().subscribe({
      next: (transactions: any[]) => {
        const nodesSet = new Set<string>();
        const fraudAccounts = new Set<string>();
        const fraudEdges: any[] = [];

        transactions.forEach(tx => {
          nodesSet.add(tx.srcId);
          nodesSet.add(tx.dstId);

          if (tx.isFraud) {
            fraudAccounts.add(tx.srcId);
            fraudAccounts.add(tx.dstId);
            fraudEdges.push(tx);
          }
        });

        this.totalNodes = nodesSet.size;
        this.totalEdges = transactions.length;
        this.fraudNodesCount = fraudAccounts.size;
        this.recentFraudConnections = fraudEdges.slice(0, 10); // Top 10 conexiones fraudulentas
        this.loading = false;
      },
      error: (err) => {
        console.error('Error procesando la red de grafos:', err);
        this.loading = false;
      }
    });
  }
}
