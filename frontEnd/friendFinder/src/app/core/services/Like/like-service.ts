import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LikeService {
  private http = inject(HttpClient);
/*
  private apiUrl = 'http://localhost:8081/api/likes';
*/
  private readonly apiUrl = `${environment.apiUrl}/likes`;


  toggleLike(postId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${postId}`, {});
  }
}
