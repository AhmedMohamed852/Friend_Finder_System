import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, ElementRef, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, of, forkJoin } from 'rxjs';
import { UserService, UserProfile } from '../../core/services/user/user-service';
import { PostService } from '../../core/services/posts/posts-service';
import { LikeService } from '../../core/services/Like/like-service';
import { FriendshipService } from '../../core/services/Friendship/friendship-service';
import { AuthService } from '../../core/services/auth/auth';
import { InterestsService } from '../../core/services/interests/interests-service';
import { InterestCategory } from '../../core/models/InterestCategory';
import { InterestsDto } from '../../core/models/InterestsDto';

export interface DisplayPost {
  id: number;
  content: string;
  postImage?: string;
  countLikes: number;
  countComments: number | null;
  timeAgo: string;
  liked: boolean;
}

type FriendshipState = 'loading' | 'none' | 'friends' | 'sent' | 'received';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit, AfterViewInit, OnDestroy {
  private route             = inject(ActivatedRoute);
  private userService       = inject(UserService);
  private postService       = inject(PostService);
  private likeService       = inject(LikeService);
  private friendshipService = inject(FriendshipService);
  private authService       = inject(AuthService);
  private interestsService  = inject(InterestsService); // 👈 حقن سيرفس الاهتمامات
  private cdr               = inject(ChangeDetectorRef);
  private router            = inject(Router);

  @ViewChild('scrollAnchor') scrollAnchor?: ElementRef<HTMLDivElement>;

  userId        = 0;
  currentUserId = 0;
  user: UserProfile | null = null;

  // 👈 مصفوفة تخزين اهتمامات المستخدم الجاية من الـ API الخارجي
  userInterests: InterestsDto[] = [];
  isLoadingInterests = false;

  posts: DisplayPost[] = [];
  currentPage  = 1;
  hasMorePosts = true;

  loadingProfile = false;
  loadingPosts   = false;
  errorProfile   = '';
  errorPosts     = '';

  selectedPostForModal: DisplayPost | null = null;
  isModalOpen = false;

  friendshipState: FriendshipState = 'loading';
  friendshipId = 0;
  actionLoading  = false;
  friendshipError = '';

  private observer?: IntersectionObserver;

  // خريطة الأيقونات المخصصة لكل تصنيف
  private categoryIcons: Record<string, string> = {
    [InterestCategory.SPORTS]: '⚽',
    [InterestCategory.TECHNOLOGY]: '💻',
    [InterestCategory.ARTS]: '🎨',
    [InterestCategory.MUSIC]: '🎵',
    [InterestCategory.MOVIES]: '🎬',
    [InterestCategory.BOOKS]: '📚',
    [InterestCategory.TRAVEL]: '✈️',
    [InterestCategory.FOOD]: '🍕',
    [InterestCategory.GAMING]: '🎮',
    [InterestCategory.FITNESS]: '🏋️',
    [InterestCategory.PHOTOGRAPHY]: '📸',
    [InterestCategory.FASHION]: '👠',
    [InterestCategory.EDUCATION]: '🎓',
    [InterestCategory.BUSINESS]: '💼',
    [InterestCategory.SCIENCE]: '🔬',
    [InterestCategory.OTHER]: '✨'
  };

  get isMe(): boolean       { return this.userId === this.currentUserId; }
  get isLoading(): boolean  { return this.friendshipState === 'loading'; }
  get isNone(): boolean     { return this.friendshipState === 'none'; }
  get isSent(): boolean     { return this.friendshipState === 'sent'; }
  get isReceived(): boolean { return this.friendshipState === 'received'; }
  get isFriend(): boolean   { return this.friendshipState === 'friends'; }

  ngOnInit(): void {
    const uid = this.authService.getCurrentUserId();
    if (uid > 0) {
      this.currentUserId = uid;
    } else {
      this.authService.fetchCurrentUser().subscribe({
        next: () => { this.currentUserId = this.authService.getCurrentUserId(); }
      });
    }

    this.route.paramMap.subscribe(params => {
      this.userId = Number(params.get('id'));
      this.resetState();
      this.loadProfile();
      this.loadUserInterests(); // 👈 استدعاء جلب الاهتمامات بالـ API الخارجي
      this.loadPosts();
      if (!this.isMe) this.loadFriendshipStatus();
    });
  }

  ngAfterViewInit(): void { this.setupObserver(); }
  ngOnDestroy(): void     { this.observer?.disconnect(); }

  private resetState(): void {
    this.user             = null;
    this.userInterests    = [];
    this.posts            = [];
    this.currentPage      = 1;
    this.hasMorePosts     = true;
    this.errorProfile     = '';
    this.errorPosts       = '';
    this.friendshipState  = 'loading';
    this.friendshipId     = 0;
    this.friendshipError  = '';
    this.actionLoading    = false;
    this.isModalOpen      = false;
    this.selectedPostForModal = null;
  }

  // 👈 دالة جلب اهتمامات المستخدم بـ API من السيرفس
  loadUserInterests(): void {
    if (!this.userId) return;
    this.isLoadingInterests = true;
    this.interestsService.getUserInterests(this.userId).subscribe({
      next: (interests) => {
        this.userInterests = interests || [];
        this.isLoadingInterests = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load user interests:', err);
        this.isLoadingInterests = false;
        this.cdr.detectChanges();
      }
    });
  }

  getInterestIcon(category: InterestCategory | string): string {
    return this.categoryIcons[category] || '🏷️';
  }

  goToUpdateProfile(): void {
    this.router.navigate(['/updateProfile']);
  }

  loadFriendshipStatus(): void {
    this.friendshipState = 'loading';
    forkJoin({
      friends:  this.friendshipService.getMyFriends().pipe(catchError(() => of([]))),
      sent:     this.friendshipService.getSentFriendRequests().pipe(catchError(() => of([]))),
      received: this.friendshipService.FriendshipRequests().pipe(catchError(() => of([])))
    }).subscribe(({ friends, sent, received }) => {
      const friendMatch = friends.find(f => f.id === this.userId || f.userSenderId === this.userId);
      const sentMatch = sent.find(f => f.id === this.userId || f.userSenderId === this.userId);
      const receivedMatch = received.find(f => f.userSenderId === this.userId || f.id === this.userId);

      if (friendMatch) {
        this.friendshipState = 'friends';
        this.friendshipId    = friendMatch.Friendship_Id;
      } else if (receivedMatch) {
        this.friendshipState = 'received';
        this.friendshipId    = receivedMatch.Friendship_Id;
      } else if (sentMatch) {
        this.friendshipState = 'sent';
        this.friendshipId    = sentMatch.Friendship_Id;
      } else {
        this.friendshipState = 'none';
        this.friendshipId    = 0;
      }
      this.cdr.detectChanges();
    });
  }

  sendFriendRequest(): void {
    if (this.actionLoading) return;
    this.actionLoading   = true;
    this.friendshipError = '';
    this.friendshipService.sendFriendRequest(this.userId).pipe(
      catchError(err => {
        this.friendshipError = err.error?.message || 'Failed to send request.';
        this.actionLoading   = false;
        this.cdr.detectChanges();
        return of(null);
      })
    ).subscribe(res => {
      this.actionLoading = false;
      if (res !== null) this.loadFriendshipStatus();
    });
  }

  cancelRequest(): void {
    if (!this.friendshipId || this.actionLoading) return;
    this.actionLoading   = true;
    this.friendshipError = '';
    const prevState      = this.friendshipState;
    const prevId         = this.friendshipId;
    this.friendshipState = 'none';
    this.friendshipId    = 0;
    this.cdr.detectChanges();

    this.friendshipService.cancelFriendRequest(prevId).pipe(
      catchError(err => {
        this.friendshipState = prevState;
        this.friendshipId    = prevId;
        this.friendshipError = err.error?.message || 'Failed to cancel request.';
        this.actionLoading   = false;
        this.cdr.detectChanges();
        return of(null);
      })
    ).subscribe(res => {
      this.actionLoading = false;
      if (res !== null) this.loadFriendshipStatus();
    });
  }

  acceptRequest(): void {
    if (!this.friendshipId || this.actionLoading) return;
    this.actionLoading   = true;
    this.friendshipError = '';
    this.friendshipService.acceptFriendRequest(this.friendshipId).pipe(
      catchError(err => {
        this.friendshipError = err.error?.message || 'Failed to accept request.';
        this.actionLoading   = false;
        this.cdr.detectChanges();
        return of(null);
      })
    ).subscribe(res => {
      this.actionLoading = false;
      if (res !== null) this.loadFriendshipStatus();
    });
  }

  rejectRequest(): void {
    if (!this.friendshipId || this.actionLoading) return;
    this.actionLoading   = true;
    this.friendshipError = '';
    const prevState      = this.friendshipState;
    const prevId         = this.friendshipId;
    this.friendshipState = 'none';
    this.friendshipId    = 0;
    this.cdr.detectChanges();

    this.friendshipService.rejectFriendRequest(prevId).pipe(
      catchError(err => {
        this.friendshipState = prevState;
        this.friendshipId    = prevId;
        this.friendshipError = err.error?.message || 'Failed to reject request.';
        this.actionLoading   = false;
        this.cdr.detectChanges();
        return of(null);
      })
    ).subscribe(res => {
      this.actionLoading = false;
      if (res !== null) this.loadFriendshipStatus();
    });
  }

  openChat(): void { this.router.navigate(['/messages', this.userId]); }

  loadProfile(): void {
    this.loadingProfile = true;
    this.userService.getUser(this.userId).subscribe({
      next: data => {
        this.user = data;
        this.loadingProfile = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.errorProfile   = err.error?.message || 'Failed to load profile';
        this.loadingProfile = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadPosts(): void {
    if (this.loadingPosts || !this.hasMorePosts) return;
    this.loadingPosts = true;
    this.postService.getUserPosts(this.userId, this.currentPage).subscribe({
      next: response => {
        this.loadingPosts = false;
        if (response?.posts?.length > 0) {
          const mapped: DisplayPost[] = response.posts.map((p: any) => ({
            id:            p.id,
            content:       p.content,
            postImage:     p.media?.length > 0 ? p.media[0].url : undefined,
            countLikes:    p.countLikes || 0,
            countComments: p.countComments,
            timeAgo:       p.localDateTime || 'Just now',
            liked:         p.likedIs || false
          }));
          this.posts = [...this.posts, ...mapped];
          this.currentPage++;
          setTimeout(() => this.setupObserver(), 100);
        } else {
          this.hasMorePosts = false;
        }
        this.cdr.detectChanges();
      },
      error: err => {
        this.loadingPosts = false;
        this.errorPosts   = err.error?.message || 'Failed to load posts';
        this.hasMorePosts = false;
        this.cdr.detectChanges();
      }
    });
  }

  toggleLike(post: DisplayPost): void {
    const wasLiked  = post.liked;
    post.liked      = !wasLiked;
    post.countLikes += wasLiked ? -1 : 1;
    this.cdr.detectChanges();
    this.likeService.toggleLike(post.id).subscribe({
      error: () => {
        post.liked      = wasLiked;
        post.countLikes += wasLiked ? 1 : -1;
        this.cdr.detectChanges();
      }
    });
  }

  get fullName(): string { return this.user ? `${this.user.firstName} ${this.user.lastName}` : ''; }
  goToPostDetails(postId: number): void { this.router.navigate(['/post', postId]); }

  openImageModal(post: DisplayPost): void {
    this.selectedPostForModal = post;
    this.isModalOpen = true;
    this.cdr.detectChanges();
  }

  closeImageModal(): void {
    this.isModalOpen = false;
    this.selectedPostForModal = null;
    this.cdr.detectChanges();
  }

  downloadImage(imageUrl: string | undefined, authorName: string): void {
    if (!imageUrl) return;
    fetch(imageUrl)
      .then(res => res.blob())
      .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${authorName.replace(/\s+/g, '_')}_profile_post.jpg`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      })
      .catch(() => window.open(imageUrl, '_blank'));
  }

  private setupObserver(): void {
    const check = setInterval(() => {
      if (this.scrollAnchor) {
        this.observer?.disconnect();
        this.observer = new IntersectionObserver(entries => {
          if (entries[0].isIntersecting && this.hasMorePosts && !this.loadingPosts) {
            this.loadPosts();
          }
        }, { threshold: 0.1 });
        this.observer.observe(this.scrollAnchor.nativeElement);
      }
    }, 300);
    setTimeout(() => clearInterval(check), 5000);
  }

  unFriend(): void {
    if (!this.friendshipId || this.actionLoading) return;

    this.actionLoading = true;
    this.friendshipError = '';

    const prevState = this.friendshipState;
    const prevId = this.friendshipId;

    this.friendshipState = 'none';
    this.friendshipId = 0;
    this.cdr.detectChanges();

    this.friendshipService.unFriend(prevId).pipe(
      catchError(err => {
        this.friendshipState = prevState;
        this.friendshipId = prevId;
        this.friendshipError = err.error?.message || 'Failed to remove friend.';
        this.actionLoading = false;
        this.cdr.detectChanges();
        return of(null);
      })
    ).subscribe(() => {
      this.actionLoading = false;
      this.friendshipState = 'none';
      this.friendshipId = 0;
      this.cdr.detectChanges();
    });
  }
}
