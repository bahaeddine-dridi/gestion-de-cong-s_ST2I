import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class ProjetService {
  private apiUrl = 'http://localhost:8080/api/projets';

  constructor(private http: HttpClient) {}

  // Récupérer tous les projets
  getProjets(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // Ajouter un projet
  addProjet(projet: { name: string, chefId: number, users: number[] }): Observable<any> {
    return this.http.post<any>(this.apiUrl, projet);
  }
}
