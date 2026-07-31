import { Component, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CreatePostRequest, PostService } from '../../../core/services/posts/posts-service';
import { UploadService } from '../../../core/services/upload/upload-service';
import { AuthService } from '../../../core/services/auth/auth';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-post.html',
  styleUrl: './create-post.css'
})
export class CreatePost {
  private postService   = inject(PostService);
  private uploadService = inject(UploadService);
  private authService   = inject(AuthService);
  private router        = inject(Router);

  content  = '';
  imageUrl = '';
  privacy  = 'PUBLIC';
  loading  = false;
  uploading = false;
  error    = '';
  selectedFile: File | null = null;

  // Current user info for avatar display
  currentUserPicture: string | null = null;
  currentUserName = '';

  constructor() {
    effect(() => {
      const user = this.authService.currentUser();
      this.currentUserPicture = user?.profilePicture ?? null;
      this.currentUserName    = user
        ? `${user.firstName ?? ''} ${user.lastName ?? ''}`.trim()
        : '';
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.uploadFile();
    }
  }

  uploadFile() {
    if (!this.selectedFile) return;
    this.uploading = true;
    this.error = '';

    this.uploadService.uploadImage(this.selectedFile).subscribe({
      next: (url) => {
        this.imageUrl = url;
        this.uploading = false;
      },
      error: (err) => {
        this.error = 'Failed to upload image. Please try again.';
        this.uploading = false;
        console.error(err);
      }
    });
  }

  submitPost() {
    if (!this.content.trim()) {
      this.error = 'Please write something before posting.';
      return;
    }

    this.loading = true;
    this.error = '';

    const postData: CreatePostRequest = {
      content: this.content,
      media: this.imageUrl ? [{ url: this.imageUrl, type: 'IMAGE' }] : [],
      privacy: this.privacy
    };

    this.postService.createPost(postData).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.loading = false;
        this.error = 'Failed to create post. Please try again.';
        console.error(err);
      }
    });
  }
}
