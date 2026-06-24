import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

// 1️⃣ تعيين واجهة المستخدم (Author Interface)
export interface Author {
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
  roles: Array<{ id: number; name: string }>;
}

// 2️⃣ تعيين واجهة البوست (Post Interface)
export interface Post {
  id: number;
  content: string;
  media: any[];
  countLikes: number;
  countComments: number | null;
  privacy: string;
  localDateTime: Date;
  author: Author;
}

export interface MediaItem {
  url: string;
  type: string;
}

export interface CreatePostRequest {
  content: string;
  media: MediaItem[];
  privacy: string;
}


// 3️⃣ واجهة كائن الـ Response الراجع بالكامل من الـ API
export interface HomeFeedResponse {
  posts: Post[];
  totalPosts: number;
}

@Injectable({
  providedIn: 'root', // تضمن توفر السيرفيس على مستوى التطبيق وقراءتها للإنترسيبتور
})
export class PostService {

  private http = inject(HttpClient); // الحقن الحديث والمضمون بنظام الأنجولر
  private apiUrl = 'http://localhost:8081/api/post';


  getHomeFeed(page: number = 1): Observable<HomeFeedResponse> {
    return this.http.get<HomeFeedResponse>(`${this.apiUrl}/getHomeFeed/${page}`);
  }

  // ضيفها جوه الـ class PostService
  createPost(post: CreatePostRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/creatPost`, post);
  }



  getUserPosts(userId: number, page: number = 0): Observable<HomeFeedResponse> {
    return this.http.get<HomeFeedResponse>(`${this.apiUrl}/getPosts/${userId}/${page}`);
  }


  getPostById(postId: number): Observable<Post> {
    return this.http.get<Post>(
      `${this.apiUrl}/getPost/${postId}`
    );



  }
}
