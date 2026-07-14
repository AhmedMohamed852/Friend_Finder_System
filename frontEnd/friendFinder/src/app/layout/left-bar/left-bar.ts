import { Component, OnInit, OnDestroy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { interval, Subject, takeUntil, catchError, of } from 'rxjs';
import { UserService, SimpleUserProfile } from '../../core/services/user/user-service';
import { AuthService } from '../../core/services/auth/auth';
import {MessagesService} from '../../core/services/Messages/message-service';

@Component({
  selector: 'app-left-bar',
  standalone: true,
  imports: [CommonModule, RouterLinkActive, RouterLink],
  templateUrl: './left-bar.html',
  styleUrl: './left-bar.css'
})
export class LeftBarComponent implements OnInit, OnDestroy {

  private userService     = inject(UserService);
  private messagesService = inject(MessagesService);
  private authService     = inject(AuthService);
  private router          = inject(Router);
  private destroy$        = new Subject<void>();

  profile: SimpleUserProfile | null = null;
  loading = false;
  unreadCount = signal(0);

  ngOnInit(): void {
    this.loadProfile();

    const userId = this.authService.getCurrentUserId();

    if (userId > 0) {
      this.startUnreadPolling(userId);
    } else {
      // Fresh login — استنى الـ userId
      this.authService.fetchCurrentUser().subscribe({
        next: () => {
          const id = this.authService.getCurrentUserId();
          if (id > 0) this.startUnreadPolling(id);
        },
        error: () => {} // silent fail — الـ badge مش critical
      });
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private startUnreadPolling(userId: number): void {
    // اجيب الـ count فوراً
    this.loadUnreadCount(userId);

    // وبعدين كل 15 ثانية
    interval(15000).pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.loadUnreadCount(userId);
    });
  }

  private loadUnreadCount(userId: number): void {
    this.messagesService.countUnreadMessages(userId).pipe(
      catchError(() => of(0))
    ).subscribe(count => this.unreadCount.set(count));
  }

  loadProfile(): void {
    this.loading = true;
    this.userService.getSimpleProfile().subscribe({
      next:  data => { this.profile = data; this.loading = false; },
      error: err  => { console.error('Failed to load profile:', err); this.loading = false; }
    });
  }

  goToMyProfile(): void {
    if (this.profile) this.router.navigate(['/profile', this.profile.id]);
  }
}
