import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
  NotificationDto,
  NotificationService
} from '../../../core/services/notification/notification-service';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notifications.html',
  styleUrls: ['./notifications.css']
})
export class NotificationsComponent implements OnInit {

  private notificationService = inject(NotificationService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  notifications: NotificationDto[] = [];
  showNotifications = false;
  loading = false;

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.loading = true;
    this.notificationService.getNotifications().subscribe({
      next: (res) => {
        this.notifications = res;
        this.loading = false;
        this.cdr.detectChanges(); // لضمان تحديث واجهة المستخدم فوراً
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
  }

  openNotification(notification: NotificationDto): void {
    this.showNotifications = false;

    switch (notification.type) {
      case 'POST_LIKED':
      case 'COMMENT':
      case 'REPLY':
        if (notification.postId) {
          this.router.navigate(['/post', notification.postId]);
        }
        break;

      case 'FRIEND_REQUEST':
      case 'FRIEND_ACCEPTED':
      case 'FRIEND_REJECT':
        if (notification.triggeredBy?.id) {
          this.router.navigate(['/profile', notification.triggeredBy.id]);
        }
        break;

      default:
        break;
    }
  }
}
