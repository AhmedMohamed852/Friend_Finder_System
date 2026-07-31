import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {InterestsService} from '../../../core/services/interests/interests-service';
import {Match, MatchService} from '../../../core/services/match/match-service';
import {FriendshipService} from '../../../core/services/Friendship/friendship-service';
import {InterestCategory} from '../../../core/models/InterestCategory';
import {InterestsDto} from '../../../core/models/InterestsDto';


@Component({
  selector: 'app-if-nothas-any-friends',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './if-nothas-any-friends.html',
  styleUrl: './if-nothas-any-friends.css',
})
export class IfNothasAnyFriends implements OnInit {
  private friendshipService = inject(FriendshipService);
  private interestsService  = inject(InterestsService);
  private matchService      = inject(MatchService);
  private router            = inject(Router);
  private cdr               = inject(ChangeDetectorRef);

  currentStep: 'interests' | 'suggestions' = 'interests';

  checkingStatus = true;
  loadingData = false;
  savingInterests = false;

  // قائمة الاهتمامات المتوفرة
  availableInterests = Object.values(InterestCategory);
  selectedInterests: string[] = [];

  // قائمة الأصدقاء المقترحين
  suggestedFriends: Match[] = [];
  addedFriendIds: Set<number> = new Set();
  addingFriendId: number | null = null;

  private categoryIcons: Record<string, string> = {
    SPORTS: '⚽', TECHNOLOGY: '💻', ARTS: '🎨', MUSIC: '🎵',
    MOVIES: '🎬', BOOKS: '📚', TRAVEL: '✈️', FOOD: '🍕',
    GAMING: '🎮', FITNESS: '🏋️', PHOTOGRAPHY: '📸', FASHION: '👠',
    EDUCATION: '🎓', BUSINESS: '💼', SCIENCE: '🔬', OTHER: '✨'
  };

  ngOnInit(): void {
    this.checkFriendsStatus();
  }

  // 1. التحقق لو عنده أصدقاء أم لا
  checkFriendsStatus(): void {
    this.friendshipService.hasAnyFriends().subscribe({
      next: (hasFriends) => {
        if (hasFriends) {
          // لو عنده أصدقاء، تحويل مباشر للهوم
          this.router.navigate(['/home']);
        } else {
          this.checkingStatus = false;
          this.cdr.detectChanges();
        }
      },
      error: () => {
        this.checkingStatus = false;
        this.cdr.detectChanges();
      }
    });
  }

  getIcon(category: string): string {
    return this.categoryIcons[category] || '🏷️';
  }

  toggleInterest(category: string): void {
    const index = this.selectedInterests.indexOf(category);
    if (index > -1) {
      this.selectedInterests.splice(index, 1);
    } else {
      this.selectedInterests.push(category);
    }
  }

  isInterestSelected(category: string): boolean {
    return this.selectedInterests.includes(category);
  }

  // الانتقال لخطوة الأصدقاء المقترحين
  goToSuggestions(): void {
    if (this.selectedInterests.length === 0) return;

    this.savingInterests = true;

    // 👈 تحويل المصفوفة النصية إلى مصفوفة من Objects تتوافق مع InterestsDto
    const interestsPayload: InterestsDto[] = this.selectedInterests.map(category => ({
      category: category as InterestCategory
    }));

    // إرسال الـ payload المعدل
    this.interestsService.setListInterests(interestsPayload).subscribe({
      next: () => {
        this.savingInterests = false;
        this.currentStep = 'suggestions';
        this.loadSuggestedFriends();
      },
      error: () => {
        this.savingInterests = false;
        this.currentStep = 'suggestions';
        this.loadSuggestedFriends();
      }
    });
  }

  // 2. جلب المقترحين عن طريق MatchService
  loadSuggestedFriends(): void {
    this.loadingData = true;
    this.matchService.findMatches().subscribe({
      next: (matches) => {
        this.suggestedFriends = matches || [];
        this.loadingData = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loadingData = false;
        this.cdr.detectChanges();
      }
    });
  }

  // إرسال طلب صداقة
  addFriend(friendId: number): void {
    this.addingFriendId = friendId;
    this.friendshipService.sendFriendRequest(friendId).subscribe({
      next: () => {
        this.addedFriendIds.add(friendId);
        this.addingFriendId = null;
        this.cdr.detectChanges();
      },
      error: () => {
        this.addingFriendId = null;
        this.cdr.detectChanges();
      }
    });
  }

  finishSetup(): void {
    this.router.navigate(['/home']);
  }
}
