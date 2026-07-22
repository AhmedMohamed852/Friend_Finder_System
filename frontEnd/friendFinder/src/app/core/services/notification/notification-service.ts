import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserSimpleDto {
  id: number;
  profilePicture: string | null;
  firstName: string;
  lastName: string;
  city: string;
}

export interface NotificationDto {
  id: number;
  content: string;
  read: boolean;
  postId: number | null;
  commentId: number | null;
  type: string;
  triggeredBy: UserSimpleDto;
  createdDate: string | null;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8081/api/notifications';

  getNotifications(): Observable<NotificationDto[]> {
    return this.http.get<NotificationDto[]>(
      `${this.apiUrl}/userNotifications`
    );
  }

  markAsRead(notificationId: number): Observable<void> {
    return this.http.put<void>(
      `${this.apiUrl}/markAsRead/${notificationId}`,
      {}
    );
  }
}
