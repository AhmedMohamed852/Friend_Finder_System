import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PostService } from '../../core/services/posts/posts-service';
import { LikeService } from '../../core/services/Like/like-service';
import { Friendship, FriendshipService, MyFriendDto } from '../../core/services/Friendship/friendship-service';

export interface DisplayPost {
  id: number;
  content: string;
  postImage?: string;
  countLikes: number;
  countComments: number | null;
  authorName: string;
  authorPicture: string;
  timeAgo: string;
  isMock?: boolean;
  liked: boolean;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private postService = inject(PostService);
  private friendshipService = inject(FriendshipService);
  private likeService = inject(LikeService);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);

  posts: DisplayPost[] = [];
  loading = false;
  error = '';
  currentPage = 1;
  hasMorePosts = true;

  friends: Friendship[] = [];
  loadingFriends = false;

  currentActiveView: 'feed' | 'friends' = 'feed';

  ngOnInit() {
    this.loadHomeFeed();
    this.loadFriendsData();
  }

  handleViewChange(view: 'feed' | 'friends') {
    this.currentActiveView = view;
    if (view === 'feed') {
      this.refreshFeed();
    } else if (view === 'friends') {
      this.loadFriendsData();
    }
    this.cdr.detectChanges();
  }

  loadHomeFeed() {
    this.loading = true;
    this.error = '';

    this.postService.getHomeFeed(this.currentPage).subscribe({
      next: (response) => {
        this.loading = false;

        if (response && response.posts && response.posts.length > 0) {
          const mapped: DisplayPost[] = response.posts.map((p: any) => ({
            id: p.id,
            content: p.content,
            postImage: p.media && p.media.length > 0 ? p.media[0].url : undefined,
            countLikes: p.countLikes || 0,
            countComments: p.countComments,
            authorName: `${p.author.firstName} ${p.author.lastName}`,
            authorPicture: p.author.profilePicture,
            timeAgo: p.localDateTime,
            isMock: false,
            liked: p.likedIs || false
          }));

          this.posts = [...this.posts, ...mapped];
          this.currentPage++;
        } else if (this.posts.length === 0) {
          this.posts = [
            {
              id: 901,
              authorName: 'Omar Khaled',
              authorPicture: 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150',
              timeAgo: '3 hours ago',
              content: 'Just finished setting up the microservices architecture for Friend Finder system! 🚀🔥',
              countLikes: 12,
              countComments: 2,
              isMock: true,
              liked: false
            },
            {
              id: 902,
              authorName: 'Youssef Ali',
              authorPicture: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150',
              timeAgo: '5 hours ago',
              content: 'The Angular standalone components with dark mode look absolutely cinematic.',
              countLikes: 8,
              countComments: 0,
              isMock: true,
              liked: false
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
        this.error = 'Failed to load home feed. Please try again.';
        console.error('Error fetching feed:', err);
        this.cdr.detectChanges();
      }
    });
  }

  toggleLike(post: DisplayPost): void {
    if (post.isMock) return;

    // Optimistic UI: نغيّر الحالة فوراً قبل ما الـ request يخلص
    const wasLiked = post.liked;
    post.liked = !wasLiked;
    post.countLikes += wasLiked ? -1 : 1;
    this.cdr.detectChanges();

    this.likeService.toggleLike(post.id).subscribe({
      error: (err) => {
        // لو الـ request فشل، نرجع الحالة زي ما كانت
        post.liked = wasLiked;
        post.countLikes += wasLiked ? 1 : -1;
        console.error('Error toggling like:', err);
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
            { Friendship_Id: 701, userSenderId: 101, firstName: 'Omar', id: 1, last_Name: 'Khaled', profilePicture: 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150' },
            { Friendship_Id: 702, userSenderId: 102, firstName: 'Youssef', id: 2, last_Name: 'Ali', profilePicture: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150' },
            { Friendship_Id: 703, userSenderId: 103, firstName: 'Kareem', id: 3, last_Name: 'Mahmoud', profilePicture: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150' }
          ];
        } else {
          this.friends = data;
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loadingFriends = false;
        console.error('Error fetching friends:', err);
        this.cdr.detectChanges();
      }
    });
  }

  viewProfile(id: number): void {
    this.router.navigate(['/profile', id]);
  }

  refreshFeed() {
    this.posts = [];
    this.currentPage = 1;
    this.hasMorePosts = true;
    this.loadHomeFeed();
  }

  sendFriendRequest(authorId: number) {
    console.log(`Sending request from Home feed to user ID: ${authorId}`);
  }


openPost(postId: number): void {
  this.router.navigate(['/post', postId]);
}
}
