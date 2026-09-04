import { Routes } from '@angular/router';
import { FraudGraphComponent } from './components/fraud-graph/fraud-graph';
import { FraudNetworkGraphComponent } from './components/fraud-network-graph/fraud-network-graph';

export const routes: Routes = [
  { path: '', component: FraudGraphComponent }, // Tu panel principal actual
  { path: 'network-graph', component: FraudNetworkGraphComponent }, // La nueva vista de grafos
  { path: '**', redirectTo: '' }
];
