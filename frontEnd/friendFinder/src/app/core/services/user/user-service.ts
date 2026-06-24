import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Role {
  id: number;
  name: string;
}

export interface UserProfile {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  gender: string;
  dateOfBirth: string;
  profilePicture: string | null;
  coverPhoto: string | null;
  bio: string | null;
  country: string;
  city: string;
  roles: Role[];
}

export interface SimpleUserProfile {
  id: number;
  profilePicture: string;
  coverPhoto: string;
  firstName: string;
  lastName: string;
  city: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8081/api/users';

  getUser(id: number): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/user/${id}`);
  }


  // جوه الـ class UserService
  getSimpleProfile(): Observable<SimpleUserProfile> {
    return this.http.get<SimpleUserProfile>(`${this.apiUrl}/simpleProfile`);
  }


  // يرجع بيانات اليوزر المسجل دخوله حالياً (بدون id في الرابط)
  getMyProfile(userId: number): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/profile`);
  }
}
