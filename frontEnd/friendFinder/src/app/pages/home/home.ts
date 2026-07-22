import { Component, OnInit, inject, ChangeDetectorRef, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { catchError, of } from 'rxjs';
import { PostService } from '../../core/services/posts/posts-service';
import { LikeService } from '../../core/services/Like/like-service';
import { Friendship, FriendshipService } from '../../core/services/Friendship/friendship-service';
import { AuthService } from '../../core/services/auth/auth';
import { NotificationDto, NotificationService } from '../../core/services/notification/notification-service';
import { SearchService } from '../../core/services/Search/search-service';
import { UploadService } from '../../core/services/upload/upload-service';
import { StoriesDto, StoryService } from '../../core/services/story/story';

export interface DisplayPost {
  id: number;
  content: string;
  postImage?: string;
  countLikes: number;
  countComments: number | null;
  authorName: string;
  authorPicture: string;
  authorId: number;
  timeAgo: string;
  isMock?: boolean;
  liked: boolean;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private postService       = inject(PostService);
  private friendshipService = inject(FriendshipService);
  private likeService       = inject(LikeService);
  private authService       = inject(AuthService);
  private notifService      = inject(NotificationService);
  private cdr               = inject(ChangeDetectorRef);
  private router            = inject(Router);
  private route             = inject(ActivatedRoute);
  private searchService     = inject(SearchService);
  private storyService      = inject(StoryService);
  private uploadService     = inject(UploadService);

  posts: DisplayPost[] = [];
  loading = false;
  error = '';
  currentPage = 1;
  hasMorePosts = true;

  searchResults  = this.searchService.searchResults;
  isSearchActive = this.searchService.isSearchActive;
  searchQuery    = this.searchService.lastQuery;

  friends: Friendship[] = [];
  loadingFriends = false;
  notifications: NotificationDto[] = [];

  friendIds = new Set<number>();
  pendingRequestIds = new Set<number>();
  currentUserId = 0;
  currentUserProfilePicture: string | null = null;

  currentActiveView: 'feed' | 'friends' | 'notifications' = 'feed';

  selectedPostForModal: DisplayPost | null = null;
  isModalOpen = false;

  myStory: StoriesDto | null = null;
  friendsStories: StoriesDto[] = [];
  isStoryModalOpen = false;
  selectedStory: StoriesDto | null = null;
  storyTimeoutId: any = null;
  isUploadingStory = false;

  constructor() {
    effect(() => {
      const currentUser = this.authService.currentUser();
      this.currentUserProfilePicture = currentUser?.profilePicture ?? null;
    });
  }

  ngOnInit() {
    this.currentUserId = this.authService.getCurrentUserId();
    this.loadHomeFeed();
    this.loadFriendsData();
    this.loadSentRequests();
    this.loadStoriesData();

    this.route.queryParams.subscribe(params => {
      if (params['view'] === 'notifications') {
        this.currentActiveView = 'notifications';
        this.loadNotificationsData();
      } else if (params['view'] === 'friends') {
        this.currentActiveView = 'friends';
      } else {
        this.currentActiveView = 'feed';
      }
      this.cdr.detectChanges();
    });
  }

  isFriend(authorId: number): boolean { return this.friendIds.has(authorId); }
  isPending(authorId: number): boolean { return this.pendingRequestIds.has(authorId); }
  isMe(authorId: number): boolean { return authorId === this.currentUserId; }

  loadStoriesData() {
    this.storyService.getMyStory().pipe(
      catchError(() => of([] as StoriesDto[]))
    ).subscribe(stories => {
      const list: StoriesDto[] = Array.isArray(stories)
        ? stories
        : (stories ? [stories] : []);
      this.myStory = list.find(s => s.user?.id === this.currentUserId) ?? null;
      this.friendsStories = list.filter(s => s.user?.id !== this.currentUserId);
      this.cdr.detectChanges();
    });
  }

  onStoryFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (!file) return;

    const mediaType: 'IMAGE' | 'VIDEO' = file.type.startsWith('video/') ? 'VIDEO' : 'IMAGE';
    this.isUploadingStory = true;
    this.cdr.detectChanges();

    this.uploadService.uploadImage(file).subscribe({
      next: (uploadedUrl: string) => {
        const newStoryData: StoriesDto = { url: uploadedUrl, type: mediaType };
        this.storyService.newStory(newStoryData).subscribe({
          next: () => {
            this.isUploadingStory = false;
            this.loadStoriesData();
          },
          error: (err) => {
            console.error('Failed to save story on backend:', err);
            this.isUploadingStory = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: (err) => {
        console.error('Failed to upload media:', err);
        this.isUploadingStory = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadHomeFeed() {
    this.loading = true;
    this.error = '';
    this.postService.getHomeFeed(this.currentPage).subscribe({
      next: (response) => {
        this.loading = false;
        if (response?.posts?.length > 0) {
          const mapped: DisplayPost[] = response.posts.map((p: any) => ({
            id:            p.id,
            content:       p.content,
            postImage:     p.media?.length > 0 ? p.media[0].url : undefined,
            countLikes:    p.countLikes || 0,
            countComments: p.countComments,
            authorName:    `${p.author.firstName} ${p.author.lastName}`,
            authorPicture: p.author.profilePicture,
            authorId:      p.author.id,
            timeAgo:       p.localDateTime,
            isMock:        false,
            liked:         p.likedIs || false
          }));
          this.posts = [...this.posts, ...mapped];
          this.currentPage++;
        } else if (this.posts.length === 0) {
          this.posts = [
            {
              id: 901, authorId: 901, authorName: 'Rodina Mohamed',
              authorPicture: 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150',
              timeAgo: '3 hours ago', content: 'Rodina Posts',
              postImage: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500',
              countLikes: 12, countComments: 2, liked: false
            }
          ];
          this.hasMorePosts = false;
        } else {
          this.hasMorePosts = false;
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Failed to load home feed.';
        this.cdr.detectChanges();
      }
    });
  }

  loadFriendsData() {
    this.loadingFriends = true;
    this.friendshipService.getMyFriends().subscribe({
      next: (data) => {
        this.loadingFriends = false;
        if (!data || data.length === 0) {
          this.friends = [
            { Friendship_Id: 701, userSenderId: 101, firstName: 'Omar', id: 1, last_Name: 'Khaled', profilePicture: 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150' }
          ];
        } else { this.friends = data; }
        this.friendIds = new Set(this.friends.map(f => f.id));
        this.cdr.detectChanges();
      },
      error: () => { this.loadingFriends = false; this.cdr.detectChanges(); }
    });
  }

  loadSentRequests() {
    this.friendshipService.getSentFriendRequests().pipe(catchError(() => of([]))).subscribe(requests => {
      this.pendingRequestIds = new Set(requests.map(r => r.id));
      this.cdr.detectChanges();
    });
  }

  loadNotificationsData() {
    this.loading = true;
    this.notifService.getNotifications().subscribe({
      next: (res) => {
        // الـ Backend بيرتب، بس كـ safety net هنرتب في الفرونت كمان
        // false (مش مقروء) = 0 → يجي قبل true (مقروء) = 1
        this.notifications = (res ?? []).sort((a, b) => {
          if (a.read === b.read) return 0;
          return a.read ? 1 : -1;
        });
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  sendFriendRequest(authorId: number) {
    if (this.isFriend(authorId) || this.isPending(authorId) || this.isMe(authorId)) return;
    this.pendingRequestIds.add(authorId);
    this.cdr.detectChanges();
    this.friendshipService.sendFriendRequest(authorId).pipe(catchError(err => {
      this.pendingRequestIds.delete(authorId);
      this.cdr.detectChanges();
      return of(null);
    })).subscribe(() => { this.loadFriendsData(); this.loadSentRequests(); });
  }

  toggleLike(post: DisplayPost): void {
    if (post.isMock) return;
    const wasLiked  = post.liked;
    post.liked      = !wasLiked;
    post.countLikes += wasLiked ? -1 : 1;
    this.cdr.detectChanges();
    this.likeService.toggleLike(post.id).subscribe({ error: () => { post.liked = wasLiked; post.countLikes += wasLiked ? 1 : -1; this.cdr.detectChanges(); } });
  }

  handleViewChange(view: 'feed' | 'friends' | 'notifications') {
    this.currentActiveView = view;
    if (view === 'feed') this.refreshFeed();
    else if (view === 'friends') this.loadFriendsData();
    else this.loadNotificationsData();
    this.cdr.detectChanges();
  }

  refreshFeed() { this.posts = []; this.currentPage = 1; this.hasMorePosts = true; this.loadHomeFeed(); }
  viewProfile(id: number): void { this.router.navigate(['/profile', id]); }
  openPost(postId: number): void { this.router.navigate(['/post', postId]); }
  openChat(authorId: number): void { this.router.navigate(['/messages', authorId]); }

  // ✅ openNotification مع markAsRead
  openNotification(notification: NotificationDto): void {

    // 1. لو مش مقروء → Optimistic Update فوراً في الـ UI
    if (!notification.read) {
      this.notifications = this.notifications.map(n =>
        n.id === notification.id ? { ...n, read: true } : n
      );
      this.cdr.detectChanges();

      // ✅ ابعت الـ request للباك في الخلفية
      this.notifService.markAsRead(notification.id).subscribe({
        error: () => {
          // فشل → رجّع الـ UI للحالة الأصلية
          this.notifications = this.notifications.map(n =>
            n.id === notification.id ? { ...n, read: false } : n
          );
          this.cdr.detectChanges();
        }
      });
    }

    // 2. Navigate فوراً من غير استنناء الـ API
    switch (notification.type) {
      case 'POST_LIKED':
      case 'COMMENT':
      case 'REPLY':
        if (notification.postId) this.router.navigate(['/post', notification.postId]);
        break;
      case 'FRIEND_REQUEST':
      case 'FRIEND_ACCEPTED':
      case 'FRIEND_REJECT':
        if (notification.triggeredBy?.id) this.router.navigate(['/profile', notification.triggeredBy.id]);
        break;
    }
  }

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

  clearSearch(): void {
    this.searchService.clearSearch();
  }

  downloadImage(imageUrl: string | undefined, authorName: string): void {
    if (!imageUrl) return;
    fetch(imageUrl)
      .then(response => response.blob())
      .then(blob => {
        const blobUrl = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = blobUrl;
        a.download = `${authorName.replace(/\s+/g, '_')}_post_image.jpg`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(blobUrl);
      })
      .catch(err => {
        console.error('تحميل الصورة فشل، سيتم فتحها في تبويب جديد لتنزيلها يدوياً:', err);
        window.open(imageUrl, '_blank');
      });
  }

  openStoryModal(story: StoriesDto) {
    this.selectedStory = story;
    this.isStoryModalOpen = true;
    this.storyTimeoutId = setTimeout(() => {
      this.closeStoryModal();
    }, 300000);
  }

  closeStoryModal() {
    this.isStoryModalOpen = false;
    this.selectedStory = null;
    if (this.storyTimeoutId) {
      clearTimeout(this.storyTimeoutId);
    }
  }
}
