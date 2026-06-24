import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommentService, CommentDto } from '../../../core/services/comments/comment-service';

@Component({
  selector: 'app-post-comments',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './comments.html',
  styleUrls: ['./comments.css'],
})
export class PostComponent implements OnInit {

  comments: CommentDto[] = [];

  private postId = 25;

  constructor(private commentService: CommentService) {}

  ngOnInit(): void {
    this.loadComments(this.postId);
  }

  loadComments(postId: number): void {
    this.commentService.getCommentsByPostId(postId)
      .subscribe({
        next: (res) => {
          this.comments = res;
        },
        error: (err) => {
          console.error(err);
        }
      });
  }

  addComment(): void {
    this.commentService.addComment({
      postId: this.postId,
      content: 'great job ❤ 🚀🔥'
    }).subscribe({
      next: () => {
        this.loadComments(this.postId);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }
}
