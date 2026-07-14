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

// 🟢 الـ Interface المقابل للـ UpdateProfileDto في الـ Backend
export interface UpdateProfileDto {
  id?: number; // اختياري لو هتجيبه من الـ Token أو الـ Auth في السيرفر، أو هتبعته صراحة
  image?: string; // لـ profile picture أو الـ cover حسب التعامل في السيرفر
  CoverPhoto?: string; // لـ profile picture أو الـ cover حسب التعامل في السيرفر
  firstName: string;
  lastName: string;
  bio?: string;
  city?: string;
  country?: string;
  dateOfBirth: string; // بيبعت بصيغة YYYY-MM-DD
  gender: 'MALE' | 'FEMALE' | string; // حسب الـ Enum اللي عندك في الـ Java
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

  getSimpleProfile(): Observable<SimpleUserProfile> {
    return this.http.get<SimpleUserProfile>(`${this.apiUrl}/simpleProfile`);
  }

  searchUsers(key: string, pageNumber: number): Observable<SimpleUserProfile[]> {
    return this.http.get<SimpleUserProfile[]>(
      `${this.apiUrl}/search/${encodeURIComponent(key)}/${pageNumber}`
    );
  }

  // 🟢 دالة تحديث الملف الشخصي
  // الـ Backend بيرجع ResponseEntity<Void> مع HttpStatus 204 (No Content)، عشان كده استخدمنا <void>
  updateProfile(profileData: UpdateProfileDto): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/updateProfile`, profileData);
  }
}
