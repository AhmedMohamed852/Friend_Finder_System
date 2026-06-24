import { Injectable, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8081/api/auth';

  // Signal لمراقبة حالة الهيدر والـ Logout فوراً
  public isLoggedIn = signal<boolean>(!!localStorage.getItem('token'));

  // دالة الـ Login وحفظ التوكن
  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem('token', response.token);
          localStorage.setItem('roles', JSON.stringify(response.roles));
          this.isLoggedIn.set(true);
        }
      })
    );
  }

  // دالة الـ Register
  signUp(userData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/signup`, userData);
  }

  // دالة الـ Logout ومسح الداتا
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('roles');
    this.isLoggedIn.set(false);
  }

  // الدالة السحرية الموحدة اللي الـ Interceptor بيناديها
  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
