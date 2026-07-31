import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UpdateProfileDto, UserService } from '../../../../core/services/user/user-service';
import { AuthService } from '../../../../core/services/auth/auth';
import { UploadService } from '../../../../core/services/upload/upload-service';
import { InterestsService } from '../../../../core/services/interests/interests-service';
import { InterestCategory } from '../../../../core/models/InterestCategory';
import { InterestsDto } from '../../../../core/models/InterestsDto';

@Component({
  selector: 'app-update-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-profile.html',
  styleUrl: './update-profile.css'
})
export class UpdateProfile implements OnInit {
  private fb               = inject(FormBuilder);
  private userService      = inject(UserService);
  private uploadService    = inject(UploadService);
  private authService      = inject(AuthService);
  private interestsService = inject(InterestsService);
  private router           = inject(Router);

  updateForm!: FormGroup;
  isSubmitting = false;

  avatarPreview: string | null = null;
  coverPreview:  string | null = null;
  isUploadingAvatar = false;
  isUploadingCover  = false;

  // حالة الاهتمامات واختفائها
  showInterestsSection = false;
  availableInterests: InterestsDto[] = [];
  selectedInterests: InterestsDto[] = [];
  isLoadingInterests = false;

  serverErrorMessage = '';
  successMessage     = '';
  currentUserId      = 0;

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

  ngOnInit(): void {
    this.initForm();
    this.loadAllInterests();
    this.loadCurrentUserData();
  }

  private initForm(): void {
    this.updateForm = this.fb.group({
      firstName:   ['', [Validators.required, Validators.maxLength(50)]],
      lastName:    ['', [Validators.required, Validators.maxLength(50)]],
      bio:         [''],
      city:        [''],
      country:     [''],
      dateOfBirth: ['', [Validators.required, this.pastDateValidator]],
      gender:      ['', [Validators.required]],
      image:       [''],
      coverPhoto:  ['']
    });
  }

  private loadAllInterests(): void {
    this.isLoadingInterests = true;
    this.interestsService.getAllInterests().subscribe({
      next: (interests) => {
        this.availableInterests = interests;
        this.isLoadingInterests = false;
      },
      error: (err) => {
        console.error('Failed to fetch interests', err);
        this.isLoadingInterests = false;
      }
    });
  }

  getInterestIcon(category: InterestCategory | string): string {
    return this.categoryIcons[category] || '🏷️';
  }

  toggleInterestsSection(): void {
    this.showInterestsSection = !this.showInterestsSection;
  }

  private pastDateValidator(control: any) {
    if (!control.value) return null;
    return new Date(control.value) < new Date() ? null : { notInPast: true };
  }

  private loadCurrentUserData(): void {
    this.currentUserId = this.authService.getCurrentUserId();
    if (this.currentUserId > 0) {
      // 1. جلب بيانات البروفايل الأساسية
      this.userService.getUser(this.currentUserId).subscribe({
        next: (userProfile: any) => {
          this.updateForm.patchValue({
            firstName:   userProfile.firstName,
            lastName:    userProfile.lastName,
            bio:         userProfile.bio,
            city:        userProfile.city,
            country:     userProfile.country,
            dateOfBirth: userProfile.dateOfBirth,
            gender:      userProfile.gender,
            image:       userProfile.profilePicture,
            coverPhoto:  userProfile.coverPhoto
          });
          this.avatarPreview = userProfile.profilePicture;
          this.coverPreview  = userProfile.coverPhoto;

          // 2. 👈 استدعاء جلب اهتمامات المستخدم بطلب منفصل عبر الـ Service
          this.loadUserInterests(this.currentUserId);
        },
        error: (err) => {
          this.serverErrorMessage = err.error?.message || 'Failed to load profile data.';
        }
      });
    }
  }

  // 👈 دالة مخصصة لجلب الاهتمامات عبر getUserInterests
  private loadUserInterests(userId: number): void {
    this.interestsService.getUserInterests(userId).subscribe({
      next: (userInterests: InterestsDto[]) => {
        if (userInterests && userInterests.length > 0) {
          this.selectedInterests = userInterests;
          this.showInterestsSection = true; // بيفتح الأكورديون تلقائياً طالما فيه اهتمامات
        }
      },
      error: (err) => {
        console.error('Failed to fetch user interests', err);
      }
    });
  }

  // 👈 دالة المقارنة المرنة (تتحقق سواء كانت الداتا راجعة Object أو Enum String)
  isInterestSelected(interest: InterestsDto): boolean {
    if (!this.selectedInterests || this.selectedInterests.length === 0) return false;

    return this.selectedInterests.some(item => {
      // لو الـ item الجاي عبارة عن String مباشرة
      if (typeof item === 'string') {
        return item === interest.category;
      }
      // لو الـ item عبارة عن Object
      return item.category === interest.category || (interest.id !== undefined && item.id === interest.id);
    });
  }

  // 👈 دالة التحديد وإلغاء التحديد
  toggleInterest(interest: InterestsDto): void {
    if (this.isInterestSelected(interest)) {
      this.selectedInterests = this.selectedInterests.filter(item => {
        const cat = typeof item === 'string' ? item : item.category;
        return cat !== interest.category;
      });
    } else {
      this.selectedInterests.push(interest);
    }
  }

  onFileSelected(event: any, type: 'avatar' | 'cover'): void {
    const file: File = event.target.files[0];
    if (!file) return;

    if (type === 'avatar') {
      this.isUploadingAvatar = true;
      this.uploadService.uploadImage(file).subscribe({
        next: (imageUrl: string) => {
          this.avatarPreview = imageUrl;
          this.updateForm.patchValue({ image: imageUrl });
          this.isUploadingAvatar = false;
        },
        error: () => {
          this.serverErrorMessage = 'Failed to upload profile picture.';
          this.isUploadingAvatar  = false;
        }
      });
    } else {
      this.isUploadingCover = true;
      this.uploadService.uploadImage(file).subscribe({
        next: (imageUrl: string) => {
          this.coverPreview = imageUrl;
          this.updateForm.patchValue({ coverPhoto: imageUrl });
          this.isUploadingCover = false;
        },
        error: () => {
          this.serverErrorMessage = 'Failed to upload cover photo.';
          this.isUploadingCover   = false;
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
    this.isSubmitting       = true;
    this.serverErrorMessage = '';
    this.successMessage     = '';

    const profileData: UpdateProfileDto = {
      id: this.currentUserId,
      ...this.updateForm.value
    };

    this.userService.updateProfile(profileData).subscribe({
      next: () => {
        this.interestsService.setListInterests(this.selectedInterests).subscribe({
          next: () => {
            this.isSubmitting   = false;
            this.successMessage = 'Your profile and interests have been updated successfully!';
            setTimeout(() => this.router.navigate(['/profile', this.currentUserId]), 2000);
          },
          error: () => {
            this.isSubmitting = false;
            this.serverErrorMessage = 'Profile updated, but failed to save interests.';
          }
        });
      },
      error: (err) => {
        this.isSubmitting = false;
        if (err.status === 400 && err.error?.errors) {
          this.serverErrorMessage = `Validation Error: ${Object.values(err.error.errors).join(', ')}`;
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
