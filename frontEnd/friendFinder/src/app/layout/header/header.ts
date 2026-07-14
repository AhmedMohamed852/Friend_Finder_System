import {
  Component, computed, inject, ElementRef,
  HostListener, OnInit, OnDestroy
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth/auth';
import { NotificationDto, NotificationService } from '../../core/services/notification/notification-service';
import { Subscription, catchError, Observable, of, Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { UserService, SimpleUserProfile } from '../../core/services/user/user-service';
import { SearchService } from '../../core/services/Search/search-service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header implements OnInit, OnDestroy {

  private authService   = inject(AuthService);
  private router        = inject(Router);
  private elRef         = inject(ElementRef);
  private notifService  = inject(NotificationService);
  private searchService = inject(SearchService);
  private userService   = inject(UserService);

  public isLoggedIn = computed(() => this.authService.isLoggedIn());

  public notifications: NotificationDto[] = [];
  public notifOpen     = false;
  public menuOpen      = false;
  public searchOpen    = false;
  public searchQuery   = '';
  public loadingNotif  = false;
  public searchLoading = false;

  private sub?: Subscription;
  private searchSub?: Subscription;
  private searchSubject = new Subject<string>();

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.loadNotifications();
    }

    // ✅ Live search بـ debounce 400ms
    this.searchSub = this.searchSubject.pipe(
      debounceTime(400),
      distinctUntilChanged()
    ).subscribe(key => {
      if (!key.trim()) {
        this.searchService.clearSearch();
        this.searchLoading = false;
        return;
      }
      this.searchLoading = true;
      this.userService.searchUsers(key.trim(), 1).pipe(
        catchError((): Observable<SimpleUserProfile[]> => of([]))
      ).subscribe((results: SimpleUserProfile[]) => {
        this.searchLoading = false;
        this.searchService.setResults(results, key.trim());
        this.router.navigate(['/home']);
      });
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    this.searchSub?.unsubscribe();
  }

  loadNotifications(): void {
    this.loadingNotif = true;
    this.sub = this.notifService.getNotifications().subscribe({
      next:  (res) => { this.notifications = res ?? []; this.loadingNotif = false; },
      error: (err) => { console.error('Notif error:', err); this.loadingNotif = false; }
    });
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
  }

  // ✅ بيتنفذ كل ما يكتب حرف
  onSearchInput(): void {
    this.searchSubject.next(this.searchQuery);
  }

  toggleMenu():   void { this.menuOpen   = !this.menuOpen; }
  closeMenu():    void { this.menuOpen   = false; }
  toggleSearch(): void { this.searchOpen = !this.searchOpen; this.notifOpen = false; }

  toggleNotifications(): void {
    this.notifOpen  = false;
    this.searchOpen = false;
    this.closeMenu();
    this.router.navigate(['/home'], { queryParams: { view: 'notifications' } });
  }

  openNotification(n: NotificationDto): void {
    this.notifOpen = false;
    if (!n.read) n.read = true;

    switch (n.type) {
      case 'POST_LIKED':
      case 'COMMENT':
      case 'REPLY':
        if (n.postId) this.router.navigate(['/post', n.postId]);
        break;
      case 'FRIEND_REQUEST':
      case 'FRIEND_ACCEPTED':
      case 'FRIEND_REJECT':
        if (n.triggeredBy?.id) this.router.navigate(['/profile', n.triggeredBy.id]);
        break;
      default:
        console.log('Unknown type:', n.type);
    }
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(e: MouseEvent): void {
    if (!this.elRef.nativeElement.contains(e.target)) {
      this.notifOpen  = false;
      this.searchOpen = false;
    }
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
