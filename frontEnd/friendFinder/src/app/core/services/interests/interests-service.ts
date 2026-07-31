import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {InterestsDto} from '../../models/InterestsDto';

@Injectable({
  providedIn: 'root',
})
export class InterestsService {
  // يمكنك تغيير هذا المسار الأساسي بحسب ملف الـ environment لديك
  private readonly baseUrl = 'http://localhost:8081/api/interests';

  constructor(private http: HttpClient) {}

  /**
   * جلب جميع الاهتمامات
   */
  getAllInterests(): Observable<InterestsDto[]> {
    return this.http.get<InterestsDto[]>(`${this.baseUrl}/getAllInterests`);
  }

  // 👈 دالة جلب اهتمامات مستخدم معين بواسطة الـ ID
  getUserInterests(userId: number): Observable<InterestsDto[]> {
    return this.http.get<InterestsDto[]>(`${this.baseUrl}/getUserInterests/${userId}`);
  }

  /**
   * حفظ أو تحديث قائمة الاهتمامات
   * @param interests قائمة الاهتمامات المراد إرسالها
   */
  setListInterests(interests: InterestsDto[]): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/setListInterests`, interests);
  }
}
