import {
  Component, computed, inject, ElementRef,
  HostListener, OnInit, OnDestroy, effect
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

  public notifications: NotificationDto[]  = [];
  public notifOpen          = false;
  public avatarDropdownOpen = false;
  public searchQuery        = '';
  public loadingNotif       = false;
  public searchLoading      = false;

  // Current user info for avatar & dropdown
  public currentUserPicture: string | null = null;
  public currentUserName    = '';
  public currentUserId      = 0;

  private sub?: Subscription;
  private searchSub?: Subscription;
  private searchSubject = new Subject<string>();

  constructor() {
    // Keep avatar in sync with auth state
    effect(() => {
      const user = this.authService.currentUser();
      this.currentUserPicture = user?.profilePicture ?? null;
      this.currentUserName    = user ? `${user.firstName ?? ''} ${user.lastName ?? ''}`.trim() : '';
      this.currentUserId      = user?.id ?? 0;
    });
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.currentUserId = this.authService.getCurrentUserId();
      this.loadNotifications();
    }

    // Live search with debounce
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
      error: ()    => { this.loadingNotif = false; }
    });
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
  }

  onSearchInput(): void {
    this.searchSubject.next(this.searchQuery);
  }

  toggleNotifications(): void {
    this.notifOpen          = !this.notifOpen;
    this.avatarDropdownOpen = false;
  }

  toggleAvatarDropdown(): void {
    this.avatarDropdownOpen = !this.avatarDropdownOpen;
    this.notifOpen          = false;
  }

  goToMyProfile(): void {
    this.avatarDropdownOpen = false;
    this.router.navigate(['/profile', this.currentUserId]);
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
    }
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(e: MouseEvent): void {
    if (!this.elRef.nativeElement.contains(e.target)) {
      this.notifOpen          = false;
      this.avatarDropdownOpen = false;
    }
  }

  onLogout(): void {
    this.avatarDropdownOpen = false;
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
