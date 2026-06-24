import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../core/services/posts/posts-service';
import { CommentService, CommentDto } from '../../../core/services/comments/comment-service';

export interface CommentDisplay {
  id: number;
  content: string;
  authorId: number;
  authorName: string;
  authorImage: string | null;
  showReplyBox: boolean;
  replies: CommentDisplay[];
  replyPage: number;
  hasMoreReplies: boolean;
  loadingReplies: boolean;
  repliesLoaded: boolean;
}

export interface PostDetailsModel {
  id: number;
  content: string;
  postImage?: string;
  countLikes: number;
  countComments: number;
  authorName: string;
  authorPicture: string;
  timeAgo: string;
  liked: boolean;
}

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-details.html',
  styleUrls: ['./post-details.css'],
})
export class PostDetails implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private postService = inject(PostService);
  private commentService = inject(CommentService);
  private cdr = inject(ChangeDetectorRef);

  postId!: number;
  post!: PostDetailsModel;
  comments: CommentDisplay[] = [];

  loading = false;
  loadingComments = false;
  submittingComment = false;
  error = '';

  currentCommentPage = 1;
  hasMoreComments = true;

  ngOnInit(): void {
    this.postId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadPost();
    this.loadComments();
  }

  // ─── Load Post ────────────────────────────────────────────
  loadPost(): void {
    this.loading = true;
    this.postService.getPostById(this.postId).subscribe({
      next: (res: any) => {
        this.post = {
          id: res.id,
          content: res.content,
          postImage: res.media?.length ? res.media[0].url : undefined,
          countLikes: res.countLikes || 0,
          countComments: res.countComments || 0,
          authorName: `${res.author.firstName} ${res.author.lastName}`,
          authorPicture: res.author.profilePicture,
          timeAgo: res.localDateTime,
          liked: res.likedIs || false,
        };
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loading = false;
        this.error = 'Failed to load post.';
        console.error(err);
        this.cdr.detectChanges();
      }
    });
  }

  // ─── Load Comments (Paginated) ────────────────────────────
  loadComments(): void {
    if (this.loadingComments || !this.hasMoreComments) return;

    this.loadingComments = true;

    this.commentService.getCommentsByPostId(this.postId, this.currentCommentPage).subscribe({
      next: (data: CommentDto[]) => {
        const mapped = data.map(c => this.mapComment(c));
        this.comments = [...this.comments, ...mapped];

        if (data.length === 0) {
          this.hasMoreComments = false;
        } else {
          this.currentCommentPage++;
        }

        this.loadingComments = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loadingComments = false;
        console.error('Error loading comments:', err);
        this.cdr.detectChanges();
      }
    });
  }

  // ─── Map CommentDto → CommentDisplay ─────────────────────
  private mapComment(c: CommentDto): CommentDisplay {
    return {
      id: c.id,
      content: c.content,
      authorId: c.author?.id,
      authorName: c.author ? `${c.author.firstName} ${c.author.lastName}` : 'Unknown',
      authorImage: c.author?.profilePicture || null,
      showReplyBox: false,
      replies: [],
      replyPage: 1,
      hasMoreReplies: (c.countComments || 0) > 0,
      loadingReplies: false,
      repliesLoaded: false,
    };
  }

  // ─── Add Comment ──────────────────────────────────────────
  addComment(text: string): void {
    if (!text.trim() || this.submittingComment) return;

    this.submittingComment = true;

    this.commentService.addComment({
      postId: this.postId,
      content: text.trim()
    }).subscribe({
      next: (res: any) => {
        const newComment: CommentDisplay = {
          id: res?.id || Date.now(),
          content: text.trim(),
          authorId: res?.author?.id || 0,
          authorName: res?.author
            ? `${res.author.firstName} ${res.author.lastName}`
            : 'You',
          authorImage: res?.author?.profilePicture || null,
          showReplyBox: false,
          replies: [],
          replyPage: 1,
          hasMoreReplies: false,
          loadingReplies: false,
          repliesLoaded: true,
        };

        this.comments.unshift(newComment);
        if (this.post) this.post.countComments++;
        this.submittingComment = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.submittingComment = false;
        console.error('Error adding comment:', err);
        this.cdr.detectChanges();
      }
    });
  }

  // ─── Toggle Reply Box ─────────────────────────────────────
  toggleReplyBox(comment: CommentDisplay): void {
    comment.showReplyBox = !comment.showReplyBox;

    if (comment.showReplyBox && !comment.repliesLoaded) {
      this.loadReplies(comment);
    }
  }

  // ─── Load Replies ─────────────────────────────────────────
  loadReplies(comment: CommentDisplay): void {
    if (comment.loadingReplies || !comment.hasMoreReplies) return;

    comment.loadingReplies = true;

    this.commentService.getReplies(comment.id, comment.replyPage).subscribe({
      next: (data: CommentDto[]) => {
        const mapped = data.map(r => this.mapComment(r));
        comment.replies = [...comment.replies, ...mapped];

        if (data.length === 0) {
          comment.hasMoreReplies = false;
        } else {
          comment.replyPage++;
        }

        comment.repliesLoaded = true;
        comment.loadingReplies = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        comment.loadingReplies = false;
        console.error('Error loading replies:', err);
        this.cdr.detectChanges();
      }
    });
  }

  // ─── Add Reply ────────────────────────────────────────────
  addReply(comment: CommentDisplay, text: string): void {
    if (!text.trim()) return;

    this.commentService.replyToComment({
      commentId: comment.id,
      content: text.trim()
    }).subscribe({
      next: (res: any) => {
        const reply: CommentDisplay = {
          id: res?.id || Date.now(),
          content: text.trim(),
          authorId: res?.author?.id || 0,
          authorName: res?.author
            ? `${res.author.firstName} ${res.author.lastName}`
            : 'You',
          authorImage: res?.author?.profilePicture || null,
          showReplyBox: false,
          replies: [],
          replyPage: 1,
          hasMoreReplies: false,
          loadingReplies: false,
          repliesLoaded: true,
        };

        comment.replies.push(reply);
        comment.showReplyBox = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error adding reply:', err);
        this.cdr.detectChanges();
      }
    });
  }

  // ─── Delete Comment ───────────────────────────────────────
  deleteComment(commentId: number): void {
    this.commentService.deleteComment(commentId).subscribe({
      next: () => {
        this.comments = this.comments.filter(c => c.id !== commentId);
        if (this.post) this.post.countComments--;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error deleting comment:', err)
    });
  }

  // ─── Navigate to Profile ──────────────────────────────────
  openProfile(authorId: number): void {
    if (authorId) {
      this.router.navigate(['/profile', authorId]);
    }
  }
}
