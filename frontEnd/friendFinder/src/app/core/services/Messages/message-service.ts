import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {SendMessagePayload} from '../../models/SendMessagePayload';
import {MessagesDto} from '../../models/MessagesDto';

@Injectable({
  providedIn: 'root'
})
export class MessagesService {

  private readonly BASE = 'http://localhost:8081/api/messages';

  constructor(private http: HttpClient) {}

  // ── POST /api/messages/send/{receiverId} ──────────────────
  sendMessage(receiverId: number, payload: SendMessagePayload): Observable<MessagesDto> {
    return this.http.post<MessagesDto>(`${this.BASE}/send/${receiverId}`, payload);
  }

  // ── GET /api/messages/{messageId} ─────────────────────────
  getMessageById(messageId: number): Observable<MessagesDto> {
    return this.http.get<MessagesDto>(`${this.BASE}/${messageId}`);
  }

  // ── GET /api/messages/conversation/{userId1}/{userId2} ─────
  getConversation(userId1: number, userId2: number): Observable<MessagesDto[]> {
    return this.http.get<MessagesDto[]>(`${this.BASE}/conversation/${userId1}/${userId2}`);
  }

  // ── GET /api/messages/inbox/{userId} ──────────────────────
  getInbox(userId: number): Observable<MessagesDto[]> {
    return this.http.get<MessagesDto[]>(`${this.BASE}/inbox/${userId}`);
  }

  // ── GET /api/messages/sent/{userId} ───────────────────────
  getSentMessages(userId: number): Observable<MessagesDto[]> {
    return this.http.get<MessagesDto[]>(`${this.BASE}/sent/${userId}`);
  }

  // ── GET /api/messages/unread/{userId} ─────────────────────
  getUnreadMessages(userId: number): Observable<MessagesDto[]> {
    return this.http.get<MessagesDto[]>(`${this.BASE}/unread/${userId}`);
  }

  // ── GET /api/messages/unread/{userId}/count ───────────────
  countUnreadMessages(userId: number): Observable<number> {
    return this.http.get<number>(`${this.BASE}/unread/${userId}/count`);
  }

  // ── PATCH /api/messages/{messageId}/read ──────────────────
  markAsRead(messageId: number): Observable<MessagesDto> {
    return this.http.put<MessagesDto>(`${this.BASE}/${messageId}/read`, {});
  }

  // ── PATCH /api/messages/conversation/{senderId}/{receiverId}/read
  markConversationAsRead(senderId: number, receiverId: number): Observable<void> {
    return this.http.put<void>(
      `${this.BASE}/conversation/${senderId}/${receiverId}/read`, {}
    );
  }

  // ── PUT /api/messages/{messageId} ─────────────────────────
  updateMessage(messageId: number, payload: SendMessagePayload): Observable<MessagesDto> {
    return this.http.put<MessagesDto>(`${this.BASE}/${messageId}`, payload);
  }

  // ── DELETE /api/messages/{messageId} ──────────────────────
  deleteMessage(messageId: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE}/${messageId}`);
  }
}
