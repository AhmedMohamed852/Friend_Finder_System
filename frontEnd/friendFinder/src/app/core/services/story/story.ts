import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// تعريف الـ Interfaces بناءً على الـ DTOs الخاصة بك
export interface UserDto {
  id: number;
  profilePicture: string;
  coverPhoto: string; // تأكد من الـ Case (سواء coverPhoto أو CoverPhoto حسب الـ mapping)
  firstName: string;
  lastName: string;
  city: string;
}

export interface StoriesDto {
  id?: number;
  url: string;
  type: 'IMAGE' | 'VIDEO'; // بناءً على الـ MediaType Enum
  user?: UserDto; // اختياري في الـ Request وإجباري في الـ Response
}

@Injectable({
  providedIn: 'root',
})
export class StoryService {
  private http = inject(HttpClient);

  // غير الـ Base URL حسب بورت المشروع عندك (مثلاً 8081)
  private apiUrl = 'http://localhost:8081/api/stories';

  constructor() {}

  /**
   * ➕ إنشاء ستوري جديدة
   * @param storyData يحتوي على الـ url والـ type فقط
   */
  newStory(storyData: StoriesDto): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/newStory`, storyData);
  }

  /**
   * 🔍 جلب الستوري الخاصة باليوزر الحالي المسجل
   */
  getMyStory(): Observable<StoriesDto> {
    return this.http.get<StoriesDto>(`${`${this.apiUrl}/getStories`}`);
  }
}
