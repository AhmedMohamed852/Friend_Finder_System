import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import {UpdateProfileDto, UserService} from '../../../../core/services/user/user-service';
import {AuthService} from '../../../../core/services/auth/auth';
import {UploadService} from '../../../../core/services/upload/upload-service';

@Component({
  selector: 'app-update-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-profile.html',
  styleUrl: './update-profile.css'
})
export class UpdateProfile implements OnInit {
  private fb          = inject(FormBuilder);
  private userService = inject(UserService);
  private uploadService = inject(UploadService); // 🟢 عملنا inject للسيرفس بتاعتك هنا
  private authService = inject(AuthService);
  private router      = inject(Router);

  updateForm!: FormGroup;
  isSubmitting = false;

  avatarPreview: string | null = null;
  coverPreview: string | null = null;
  isUploadingAvatar = false;
  isUploadingCover = false;

  serverErrorMessage = '';
  successMessage = '';
  currentUserId = 0;

  ngOnInit(): void {
    this.initForm();
    this.loadCurrentUserData();
  }

  private initForm(): void {
    this.updateForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      bio: [''],
      city: [''],
      country: [''],
      dateOfBirth: ['', [Validators.required, this.pastDateValidator]],
      gender: ['', [Validators.required]],
      image: [''],
      coverPhoto: ['']
    });
  }

  private pastDateValidator(control: any) {
    if (!control.value) return null;
    const inputDate = new Date(control.value);
    const today = new Date();
    return inputDate < today ? null : { notInPast: true };
  }

  private loadCurrentUserData(): void {
    this.currentUserId = this.authService.getCurrentUserId();
    if (this.currentUserId > 0) {
      this.userService.getUser(this.currentUserId).subscribe({
        next: (userProfile) => {
          this.updateForm.patchValue({
            firstName: userProfile.firstName,
            lastName: userProfile.lastName,
            bio: userProfile.bio,
            city: userProfile.city,
            country: userProfile.country,
            dateOfBirth: userProfile.dateOfBirth,
            gender: userProfile.gender,
            image: userProfile.profilePicture,
            coverPhoto: userProfile.coverPhoto
          });
          this.avatarPreview = userProfile.profilePicture;
          this.coverPreview = userProfile.coverPhoto;
        },
        error: (err) => {
          this.serverErrorMessage = err.error?.message || 'Failed to load profile data.';
        }
      });
    }
  }

  // ── 📸 تحديث منطق الرفع ليتوافق مع الـ UploadService الجديدة ──
  onFileSelected(event: any, type: 'avatar' | 'cover'): void {
    const file: File = event.target.files[0];
    if (!file) return;

    if (type === 'avatar') {
      this.isUploadingAvatar = true;
      this.uploadService.uploadImage(file).subscribe({
        next: (imageUrl: string) => { // 🟢 الراجع هنا string مباشر (الـ URL)
          this.avatarPreview = imageUrl;
          this.updateForm.patchValue({ image: imageUrl });
          this.isUploadingAvatar = false;
        },
        error: (err) => {
          this.serverErrorMessage = 'Failed to upload profile picture to ImgBB.';
          this.isUploadingAvatar = false;
        }
      });
    } else {
      this.isUploadingCover = true;
      this.uploadService.uploadImage(file).subscribe({
        next: (imageUrl: string) => { // 🟢 الراجع هنا string مباشر (الـ URL)
          this.coverPreview = imageUrl;
          this.updateForm.patchValue({ coverPhoto: imageUrl });
          this.isUploadingCover = false;
        },
        error: (err) => {
          this.serverErrorMessage = 'Failed to upload cover photo to ImgBB.';
          this.isUploadingCover = false;
        }
      });
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.updateForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  onSubmit(): void {
    if (this.updateForm.invalid) return;

    this.isSubmitting = true;
    this.serverErrorMessage = '';
    this.successMessage = '';

    const profileData: UpdateProfileDto = {
      id: this.currentUserId,
      ...this.updateForm.value
    };

    this.userService.updateProfile(profileData).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = 'Your profile has been updated successfully!';
        setTimeout(() => {
          this.router.navigate(['/profile', this.currentUserId]);
        }, 2000);
      },
      error: (err) => {
        this.isSubmitting = false;
        if (err.status === 400 && err.error?.errors) {
          const validationErrors = Object.values(err.error.errors).join(', ');
          this.serverErrorMessage = `Validation Error: ${validationErrors}`;
        } else {
          this.serverErrorMessage = err.error?.message || 'An error occurred. Please try again.';
        }
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/profile', this.currentUserId]);
  }
}
