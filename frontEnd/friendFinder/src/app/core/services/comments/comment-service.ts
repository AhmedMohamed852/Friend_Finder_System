import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CommentAuthor {
  id: number;
  firstName: string;
  lastName: string;
  profilePicture: string | null;
  city: string;
}

export interface CommentDto {
  id: number;
  content: string;
  author: CommentAuthor;
  postId: number;
  likedIs: number | null;
  countComments: number | null;
  localDateTime: string;
}

export interface NewCommentRequest {
  postId: number;
  content: string;
}

export interface UpdateCommentRequest {
  commentId: number;
  content: string;
}

export interface ReplyRequest {
  commentId: number;
  content: string;
}

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8081/api/comments';

  getCommentsByPostId(postId: number, pageNumber: number = 1): Observable<CommentDto[]> {
    const params = new HttpParams().set('pageNumber', pageNumber.toString());
    return this.http.get<CommentDto[]>(
      `${this.API_URL}/CommentsByPostId/${postId}`,
      { params }
    );
  }

  addComment(request: NewCommentRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/newComment`, request);
  }

  updateComment(request: UpdateCommentRequest): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/updateComment`, request);
  }

  replyToComment(request: ReplyRequest): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/replyToComment`, request);
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/deleteComment/${commentId}`);
  }

  getReplies(commentId: number, pageNumber: number = 1): Observable<CommentDto[]> {
    const params = new HttpParams().set('pageNumber', pageNumber.toString());
    return this.http.get<CommentDto[]>(
      `${this.API_URL}/replies/${commentId}`,
      { params }
    );
  }
}
