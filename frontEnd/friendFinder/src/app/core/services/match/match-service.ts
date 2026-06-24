import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Match {
  profilePicture: string;
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  city: string;
  country: string;
}

@Injectable({
  providedIn: 'root' // 👈 إجبارية عشان السيرفيس تورث الـ Interceptors العامة
})
export class MatchService {
  private http = inject(HttpClient); // 👈 الحقن الحديث المضمون
  private apiUrl = 'http://localhost:8081/api/matches';

  findMatches(): Observable<Match[]> {
    return this.http.get<Match[]>(`${this.apiUrl}/find`);
  }

}
