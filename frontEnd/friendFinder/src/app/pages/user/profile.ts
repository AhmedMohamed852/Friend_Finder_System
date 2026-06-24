import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, ElementRef, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import { UserService, UserProfile } from '../../core/services/user/user-service';
import { PostService } from '../../core/services/posts/posts-service';
import { LikeService } from '../../core/services/Like/like-service';

export interface DisplayPost {
  id: number;
  content: string;
  postImage?: string;
  countLikes: number;
  countComments: number | null;
  timeAgo: string;
  liked: boolean;
}

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit, AfterViewInit, OnDestroy {
  private route = inject(ActivatedRoute);
  private userService = inject(UserService);
  private postService = inject(PostService);
  private likeService = inject(LikeService);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);

  @ViewChild('scrollAnchor') scrollAnchor?: ElementRef<HTMLDivElement>;

  userId!: number;
  user: UserProfile | null = null;

  posts: DisplayPost[] = [];
  currentPage = 1;

  loadingProfile = false;
  loadingPosts = false;
  errorProfile = '';
  errorPosts = '';

  hasMorePosts = true;

  private observer?: IntersectionObserver;

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const newId = Number(params.get('id'));

      this.userId = newId;
      this.user = null;
      this.posts = [];
      this.currentPage = 1;
      this.hasMorePosts = true;
      this.errorProfile = '';
      this.errorPosts = '';

      this.loadProfile();
      this.loadPosts();
    });
  }

  ngAfterViewInit(): void {
    this.setupObserver();
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }

  private setupObserver(): void {
    const checkAnchor = setInterval(() => {
      if (this.scrollAnchor) {
        this.observer?.disconnect();

        this.observer = new IntersectionObserver((entries) => {
          if (entries[0].isIntersecting && this.hasMorePosts && !this.loadingPosts) {
            this.loadPosts();
          }
        }, { threshold: 0.1 });

        this.observer.observe(this.scrollAnchor.nativeElement);
      }
    }, 300);

    setTimeout(() => clearInterval(checkAnchor), 5000);
  }

  loadProfile(): void {
    this.loadingProfile = true;
    this.errorProfile = '';

    this.userService.getUser(this.userId).subscribe({
      next: (data) => {
        this.user = data;
        this.loadingProfile = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorProfile = 'Failed to load profile';
        this.loadingProfile = false;
        console.error(err);
        this.cdr.detectChanges();
      }
    });
  }

  loadPosts(): void {
    if (this.loadingPosts || !this.hasMorePosts) return;

    this.loadingPosts = true;
    this.errorPosts = '';

    this.postService.getUserPosts(this.userId, this.currentPage).subscribe({
      next: (response) => {
        this.loadingPosts = false;

        if (response && response.posts && response.posts.length > 0) {
          const mapped: DisplayPost[] = response.posts.map((p: any) => ({
            id: p.id,
            content: p.content,
            postImage: p.media && p.media.length > 0 ? p.media[0].url : undefined,
            countLikes: p.countLikes || 0,
            countComments: p.countComments,
            timeAgo: 'Just now',
            liked: p.likedIs || false
          }));

          this.posts = [...this.posts, ...mapped];
          this.currentPage++;

          setTimeout(() => this.setupObserver(), 100);
        } else {
          this.hasMorePosts = false;
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loadingPosts = false;
        this.errorPosts = 'Failed to load posts';
        this.hasMorePosts = false; // ⬅️ السطر الجديد: يوقف المحاولات بعد أول فشل
        console.error(err);
        this.cdr.detectChanges();
      }
    });
  }

  toggleLike(post: DisplayPost): void {
    const wasLiked = post.liked;
    post.liked = !wasLiked;
    post.countLikes += wasLiked ? -1 : 1;
    this.cdr.detectChanges();

    this.likeService.toggleLike(post.id).subscribe({
      error: (err) => {
        post.liked = wasLiked;
        post.countLikes += wasLiked ? 1 : -1;
        console.error('Error toggling like:', err);
        this.cdr.detectChanges();
      }
    });
  }

  get fullName(): string {
    return this.user ? `${this.user.firstName} ${this.user.lastName}` : '';
  }


  goToPostDetails(postId: number): void {
    // هيروح للمسار: /post-details/21 (مثلاً)
    this.router.navigate(['/post-details', postId]);
  }
}
