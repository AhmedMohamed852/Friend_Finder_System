import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, catchError, throwError } from 'rxjs';

// ── Types ────────────────────────────────────────────────────

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  roles: string[];
}

export interface CurrentUser {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  profilePicture?: string;
  coverPhoto?: string;
  city?: string;
  roles: string[];
}

interface JwtPayload {
  sub: string;   // username
  iat: number;
  exp: number;
  roles: string[];
}

// ── Service ──────────────────────────────────────────────────

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly TOKEN_KEY = 'ff_token';
  private readonly USER_KEY  = 'ff_user';
  private readonly BASE      = 'http://localhost:8081/api';

  // Signal يحمل بيانات اليوزر الحالي — أي component يقدر يقرأ منه
  currentUser = signal<CurrentUser | null>(this.loadUserFromStorage());

  // Computed helpers
  isLoggedIn = computed(() => !!this.currentUser());
  isAdmin    = computed(() => {
    const user = this.currentUser();
    if (!user) return false;
    return user.roles.some(r => r.toLowerCase().includes('admin'));
  });

  constructor(private http: HttpClient, private router: Router) {}

  // ── LOGIN ─────────────────────────────────────────────────
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.BASE}/auth/login`, credentials).pipe(
      tap(res => {
        localStorage.setItem(this.TOKEN_KEY, res.token);

        // Decode JWT للحصول على username + roles
        const payload  = this.decodeToken(res.token);
        const username = payload?.sub ?? '';
        const roles    = payload?.roles ?? [];

        // حفظ مبدئي (id = 0) ريثما يجي الـ simpleProfile
        this.setUser({ id: 0, username, firstName: '', lastName: '', roles });

        // جيب الـ profile الكامل بما فيه الـ id
        this.fetchCurrentUser().subscribe();
      }),
      catchError(err => throwError(() => err))
    );
  }

  // ── SIGNUP ───────────────────────────────────────────────
  signUp(data: any): Observable<any> {
    return this.http.post(`${this.BASE}/auth/register`, data).pipe(
      catchError(err => throwError(() => err))
    );
  }

  // ── LOGOUT ───────────────────────────────────────────────
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  // ── FETCH FULL PROFILE (GET /api/users/simpleProfile) ────
  fetchCurrentUser(): Observable<any> {
    return this.http.get<any>(`${this.BASE}/users/simpleProfile`).pipe(
      tap(profile => {
        const existing = this.currentUser();
        this.setUser({
          id:             profile.id,
          username:       existing?.username ?? '',
          firstName:      profile.firstName  ?? '',
          lastName:       profile.lastName   ?? '',
          profilePicture: profile.profilePicture ?? undefined,
          coverPhoto:     profile.coverPhoto     ?? undefined,
          city:           profile.city           ?? undefined,
          roles:          existing?.roles ?? []
        });
      }),
      catchError(err => {
        console.warn('Could not fetch current user profile:', err);
        return throwError(() => err);
      })
    );
  }

  // ── HELPERS ──────────────────────────────────────────────

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isTokenValid(): boolean {
    const token = this.getToken();
    if (!token) return false;
    const payload = this.decodeToken(token);
    if (!payload) return false;
    return payload.exp * 1000 > Date.now();
  }

  getCurrentUserId(): number {
    return this.currentUser()?.id ?? 0;
  }

  getCurrentUsername(): string {
    return this.currentUser()?.username ?? '';
  }

  // ── PRIVATE ──────────────────────────────────────────────

  private setUser(user: CurrentUser): void {
    this.currentUser.set(user);
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  private loadUserFromStorage(): CurrentUser | null {
    try {
      const raw = localStorage.getItem(this.USER_KEY);
      return raw ? (JSON.parse(raw) as CurrentUser) : null;
    } catch {
      return null;
    }
  }

  private decodeToken(token: string): JwtPayload | null {
    try {
      return JSON.parse(atob(token.split('.')[1])) as JwtPayload;
    } catch {
      return null;
    }
  }
}
