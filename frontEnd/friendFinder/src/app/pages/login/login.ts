import { ChangeDetectorRef, Component, inject, NgZone } from '@angular/core'; // 👈 استيراد NgZone
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth/auth';
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

  private cdr            = inject(ChangeDetectorRef);
  private ngZone         = inject(NgZone); // 👈 حقن الـ NgZone باستخدام inject()

  // عشان نقدر نلغي التايمر القديم لو حصل error جديد قبل ما الـ 5 ثواني تخلص
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

  onSubmit() {
    // لو الفورم غلط، اعرض الـ errors ووقف — من غير ما تشغل loading أصلاً
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.cdr.detectChanges();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).pipe(
      timeout(10000) // لو الباك ما ردش في 10 ثواني، اعتبره فشل
    ).subscribe({
      next: () => {
        this.ngZone.run(() => { // 👈 تشغيل التوجيه إلى الصفحة الرئيسية داخل الـ NgZone
          this.loading = false;
          this.router.navigate(['/home']);
        });
      },
      error: (err) => {
        this.ngZone.run(() => { // 👈 تشغيل الـ Error State داخل الـ NgZone لضمان تحديث الـ Loader فوراً
          this.loading = false;

          if (err instanceof TimeoutError) {
            this.showError('الاتصال بالسيرفر بطيء، حاول تاني.');
            return;
          }

          // الباك بيرجع { message_ar: '...', message_en: '...' }
          const message = err.error?.message_ar
            || err.error?.message_en
            || err.error?.message
            || 'Invalid username or password';

          this.showError(message);
        });
      }
    });
  }

  // بتعرض رسالة الخطأ فوراً، وتخفيها تلقائياً بعد 5 ثواني
  private showError(message: string): void {
    // لو فيه تايمر شغال من قبل، نلغيه الأول عشان منعملش تعارض
    if (this.errorTimeoutId) {
      clearTimeout(this.errorTimeoutId);
    }

    this.errorMessage = message;
    this.cdr.detectChanges(); // تحديث الفيو فوراً لظهور الرسالة

    // تشغيل الـ setTimeout داخل الـ NgZone لضمان اختفاء الرسالة تلقائياً بعد 5 ثواني
    this.errorTimeoutId = setTimeout(() => {
      this.ngZone.run(() => {
        this.errorMessage = '';
        this.errorTimeoutId = null;
        this.cdr.detectChanges(); // تحديث الفيو لإخفاء الرسالة
      });
    }, 5000); // 5 ثواني
  }
}
