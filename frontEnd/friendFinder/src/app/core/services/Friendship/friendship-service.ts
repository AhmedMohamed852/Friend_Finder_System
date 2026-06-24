import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Friendship {
  Friendship_Id: number;
  id: number;
  userSenderId: number;
  profilePicture: string;
  firstName: string;
  last_Name: string;
}
// ضيف الواجهة دي فوق في ملف السيرفيس لتطابق الـ DTO بالظبط
export interface MyFriendDto {
  Friendship_Id: number;
  id: number;
  userSenderId: number;
  profilePicture: string | null;
  firstName: string;
  last_Name: string; // متناسق مع مسمى الباك-إند عندك
}
@Injectable({
  providedIn: 'root',
})
export class FriendshipService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8081/api/v1/friendship';

  // 1️⃣ جلب طلبات الصداقة الواردة الحالية
  FriendshipRequests(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(`${this.apiUrl}/show_FriendshipRequests`);
  }

  // 2️⃣ إرسال طلب صداقة جديد (POST)
  sendFriendRequest(userReceivedId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/send-FriendRequest/${userReceivedId}`, {});
  }

  // 3️⃣ قبول طلب الصداقة (PUT)
  acceptFriendRequest(friendshipId: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/accept-FriendRequest/${friendshipId}`, {});
  }

  // 4️⃣ رفض طلب الصداقة (PUT)
  rejectFriendRequest(friendshipId: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/reject-FriendRequest/${friendshipId}`, {});
  }

  // 5️⃣ إلغاء طلب الصداقة المرسل (DELETE)
  cancelFriendRequest(friendshipId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/cancel-FriendRequest/${friendshipId}`);
  }

  // جلب الطلبات اللي أنا بعتها ولسه معلقة من الباك-إند الحقيقي
  getSentFriendRequests(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(`${this.apiUrl}/SentFriendshipRequests`); // 👈 تم التحديث بالملي لتطابق الباك إند
  }


  // ضيف الدالة دي جوه كلاس FriendshipService
  getMyFriends(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(`${this.apiUrl}/getMyFriends`);
  }
}
