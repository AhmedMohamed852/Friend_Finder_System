import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface Friendship {
  Friendship_Id: number;
  id: number;
  userSenderId: number;
  profilePicture: string;
  firstName: string;
  last_Name: string;
}

export interface MyFriendDto {
  Friendship_Id: number;
  id: number;
  userSenderId: number;
  profilePicture: string | null;
  firstName: string;
  last_Name: string;
}

export interface UserSimpleDto {
  id: number;
  profilePicture: string | null;
  coverPhoto: string | null;
  firstName: string;
  lastName: string;
  city: string;
}

@Injectable({
  providedIn: 'root',
})
export class FriendshipService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8081/api/v1/friendship';

  // ✅ إدارة حالة البحث المركزي باستخدام الـ Signals داخل الخدمة
  searchResults  = signal<UserSimpleDto[]>([]);
  isSearchActive = signal<boolean>(false);
  lastQuery      = signal<string>('');

  FriendshipRequests(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(`${this.apiUrl}/show_FriendshipRequests`);
  }

  sendFriendRequest(userReceivedId: number): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/send-FriendRequest/${userReceivedId}`,
      {}
    );
  }

  acceptFriendRequest(friendshipId: number): Observable<any> {
    return this.http.put<any>(
      `${this.apiUrl}/accept-FriendRequest/${friendshipId}`,
      {}
    );
  }

  rejectFriendRequest(friendshipId: number): Observable<any> {
    return this.http.put<any>(
      `${this.apiUrl}/reject-FriendRequest/${friendshipId}`,
      {}
    );
  }

  cancelFriendRequest(friendshipId: number): Observable<any> {
    return this.http.delete<any>(
      `${this.apiUrl}/cancel-FriendRequest/${friendshipId}`
    );
  }

  getSentFriendRequests(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(
      `${this.apiUrl}/SentFriendshipRequests`
    );
  }

  getMyFriends(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(
      `${this.apiUrl}/getMyFriends`
    );
  }

  unFriend(friendshipId: number): Observable<any> {
    return this.http.delete<any>(
      `${this.apiUrl}/unFriend/${friendshipId}`
    );
  }

  // 🔍 تحديث دالة البحث لتحديث الـ Signals تلقائياً عند طلب البحث
  search(key: string): Observable<UserSimpleDto[]> {
    this.isSearchActive.set(true);
    this.lastQuery.set(key);

    return this.http.get<UserSimpleDto[]>(
      `${this.apiUrl}/search/${encodeURIComponent(key)}`
    ).pipe(
      tap(results => {
        this.searchResults.set(results || []);
      })
    );
  }

  // ✕ دالة لتنظيف حالة البحث والعودة للـ Feed الطبيعي
  clearSearch(): void {
    this.searchResults.set([]);
    this.isSearchActive.set(false);
    this.lastQuery.set('');
  }
}
