import { Component, OnInit, OnDestroy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { interval, Subject, takeUntil, catchError, of } from 'rxjs';
import { UserService, SimpleUserProfile } from '../../core/services/user/user-service';
import { AuthService } from '../../core/services/auth/auth';
import { MessagesService } from '../../core/services/Messages/message-service';
import { NotificationService, NotificationDto } from '../../core/services/notification/notification-service';

@Component({
  selector: 'app-left-bar',
  standalone: true,
  imports: [CommonModule, RouterLinkActive, RouterLink, FormsModule],
  templateUrl: './left-bar.html',
  styleUrl: './left-bar.css'
})
export class LeftBarComponent implements OnInit, OnDestroy {

  private userService        = inject(UserService);
  private messagesService    = inject(MessagesService);
  private authService        = inject(AuthService);
  private notificationService = inject(NotificationService);
  private router             = inject(Router);
  private destroy$           = new Subject<void>();

  // Profile State
  profile: SimpleUserProfile | null = null;
  loading = false;

  // Unread Messages Badge
  unreadCount = signal(0);

  // Search State
  searchQuery = '';
  searchLoading = false;

  // Notifications State
  notifOpen = false;
  loadingNotif = false;
  unreadNotifCount = 0;
  notifications: NotificationDto[] = [];

  ngOnInit(): void {
    this.loadProfile();

    const userId = this.authService.getCurrentUserId();

    if (userId > 0) {
      this.startUnreadPolling(userId);
    } else {
      this.authService.fetchCurrentUser().subscribe({
        next: () => {
          const id = this.authService.getCurrentUserId();
          if (id > 0) this.startUnreadPolling(id);
        },
        error: () => {}
      });
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private startUnreadPolling(userId: number): void {
    this.loadUnreadCount(userId);

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

  // Search Handlers
  onSearchInput(): void {
    if (!this.searchQuery.trim()) return;
    this.searchLoading = true;
    setTimeout(() => this.searchLoading = false, 500);
  }

  onSearchFocus(): void {
    // Handling search focus
  }

  // Notifications Handlers
  onNotificationsClick(): void {
    this.notifOpen = !this.notifOpen;

    // يوجه للهوم بيدج ويعرض تبويب الإشعارات
    this.router.navigate(['/home'], { queryParams: { view: 'notifications' } });

    if (this.notifOpen && this.notifications.length === 0) {
      this.loadNotifications();
    }
  }

  loadNotifications(): void {
    this.loadingNotif = true;
    this.notificationService.getNotifications().subscribe({
      next: (res) => {
        this.notifications = res ?? [];
        this.unreadNotifCount = this.notifications.filter(n => !n.read).length;
        this.loadingNotif = false;
      },
      error: (err) => {
        console.error('Failed to load notifications:', err);
        this.loadingNotif = false;
      }
    });
  }

  openNotification(notif: NotificationDto): void {
    if (!notif.read) {
      notif.read = true;
      if (this.unreadNotifCount > 0) this.unreadNotifCount--;
      this.notificationService.markAsRead(notif.id).subscribe();
    }

    this.notifOpen = false;

    // Navigation logic بناءً على بيانات الإشعار
    if (notif.postId) {
      this.router.navigate(['/post', notif.postId]);
    } else if (notif.triggeredBy?.id) {
      this.router.navigate(['/profile', notif.triggeredBy.id]);
    } else {
      this.router.navigate(['/home'], { queryParams: { view: 'notifications' } });
    }
  }

  // Logout Handler
  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
