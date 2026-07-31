import { ChangeDetectorRef, Component, inject, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth/auth';
import { FriendshipService } from '../../core/services/Friendship/friendship-service';
import { timeout } from 'rxjs/operators';
import { TimeoutError } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  errorMessage = '';
  showPassword = false;

  private cdr            = inject(ChangeDetectorRef);
  private ngZone         = inject(NgZone);
  private friendshipService = inject(FriendshipService);
  private errorTimeoutId: any = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.cdr.detectChanges();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).pipe(
      timeout(10000)
    ).subscribe({
      next: () => {
        // فحص حالة الأصدقاء بعد اللوجين لتحديد مسار التوجيه
        this.friendshipService.hasAnyFriends().subscribe({
          next: (hasFriends) => {
            this.ngZone.run(() => {
              this.loading = false;
              if (hasFriends) {
                this.router.navigate(['/home']);
              } else {
                this.router.navigate(['/welcome']);
              }
            });
          },
          error: () => {
            // كخيار احتياطي في حالة حدوث خطأ أثناء الفحص
            this.ngZone.run(() => {
              this.loading = false;
              this.router.navigate(['/home']);
            });
          }
        });
      },
      error: (err) => {
        this.ngZone.run(() => {
          this.loading = false;

          if (err instanceof TimeoutError) {
            this.showError('الاتصال بالسيرفر بطيء، حاول تاني.');
            return;
          }

          const message = err.error?.message_ar
            || err.error?.message_en
            || err.error?.message
            || 'Invalid username or password';

          this.showError(message);
        });
      }
    });
  }

  private showError(message: string): void {
    if (this.errorTimeoutId) {
      clearTimeout(this.errorTimeoutId);
    }

    this.errorMessage = message;
    this.cdr.detectChanges();

    this.errorTimeoutId = setTimeout(() => {
      this.ngZone.run(() => {
        this.errorMessage = '';
        this.errorTimeoutId = null;
        this.cdr.detectChanges();
      });
    }, 5000);
  }
}
