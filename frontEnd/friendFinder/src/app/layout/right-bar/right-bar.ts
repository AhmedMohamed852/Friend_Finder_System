import { Component, OnInit, OnDestroy, inject, ChangeDetectorRef } from '@angular/core'; // 1. استيراد ChangeDetectorRef
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Subscription, interval } from 'rxjs';
import { MatchService, Match as BaseMatch } from '../../core/services/match/match-service';
import { FriendshipService, Friendship } from '../../core/services/Friendship/friendship-service';

export interface ExtendedMatch extends BaseMatch {
  isMock?: boolean;
  isPending?: boolean;
  friendshipId?: number;
}

@Component({
  selector: 'app-right-bar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './right-bar.html',
  styleUrl: './right-bar.css'
})
export class RightBarComponent implements OnInit, OnDestroy {

  private matchService = inject(MatchService);
  private friendshipService = inject(FriendshipService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef); // 2. حقن الخدمة هنا

  matches: ExtendedMatch[] = [];
  friendRequests: Friendship[] = [];
  sentRequests: Friendship[] = [];

  activeTab: 'received' | 'sent' = 'received';
  loading = false;
  error = '';

  private autoRefreshSub?: Subscription;
  private readonly AUTO_REFRESH_INTERVAL = 5 * 60 * 1000;

  ngOnInit(): void {
    this.refreshAll();
    this.startAutoRefresh();
  }

  ngOnDestroy(): void {
    this.autoRefreshSub?.unsubscribe();
  }

  private startAutoRefresh(): void {
    this.autoRefreshSub = interval(this.AUTO_REFRESH_INTERVAL).subscribe(() => {
      this.refreshAll();
    });
  }

  refreshAll(): void {
    this.loadMatches();
    this.loadFriendRequests();
    this.loadSentRequests();
  }

  openProfile(id: number): void {
    this.router.navigate(['/profile', id]);
  }

  switchTab(tab: 'received' | 'sent'): void {
    this.activeTab = tab;
    this.cdr.detectChanges(); // تحديث الواجهة عند تنقل التبويبات
  }

  loadMatches(): void {
    this.loading = true;
    this.error = '';

    this.matchService.findMatches().subscribe({
      next: (data: BaseMatch[]) => {
        this.loading = false;
        if (!data || data.length === 0) {
          this.matches = [
            { id: 101, username: 'omar99', firstName: 'Omar', lastName: 'Khaled', city: 'Alexandria', profilePicture: 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150', country: 'Egypt', isMock: true, isPending: false },
            { id: 102, username: 'youssef_dev', firstName: 'Youssef', lastName: 'Ali', city: 'Giza', profilePicture: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150', country: 'Egypt', isMock: true, isPending: false }
          ];
        } else {
          this.matches = data.map(item => ({ ...item, isPending: false }));
        }
        this.cdr.detectChanges(); // 3. إجبار الواجهة على التحديث
      },
      error: (err: any) => {
        this.loading = false;
        this.error = 'Failed to load suggestions';
        console.error(err);
        this.cdr.detectChanges();
      }
    });
  }

  loadFriendRequests(): void {
    this.friendshipService.FriendshipRequests().subscribe({
      next: (data: Friendship[]) => {
        if (!data || data.length === 0) {
          this.friendRequests = [
            { Friendship_Id: 201, id: 501, userSenderId: 501, firstName: 'Mostafa', last_Name: 'Amr', profilePicture: 'https://randomuser.me/api/portraits/men/32.jpg' }
          ];
        } else {
          this.friendRequests = data;
        }
        this.cdr.detectChanges(); // 3. إجبار الواجهة على التحديث
      },
      error: (err: any) => console.error(err)
    });
  }

  loadSentRequests(): void {
    this.friendshipService.getSentFriendRequests().subscribe({
      next: (data: Friendship[]) => {
        if (!data || data.length === 0) {
          this.sentRequests = [
            { Friendship_Id: 301, id: 601, userSenderId: 601, firstName: 'Kareem', last_Name: 'Hany', profilePicture: 'https://randomuser.me/api/portraits/men/22.jpg' }
          ];
        } else {
          this.sentRequests = data;
        }
        this.cdr.detectChanges(); // 3. إجبار الواجهة على التحديث
      },
      error: (err: any) => console.error('Error fetching sent requests:', err)
    });
  }

  addFriend(person: ExtendedMatch): void {
    if (person.isMock) {
      person.isPending = true;
      person.friendshipId = Math.floor(Math.random() * 1000);
      this.sentRequests.push({
        Friendship_Id: person.friendshipId,
        id: person.id,
        userSenderId: person.id,
        firstName: person.firstName,
        last_Name: person.lastName,
        profilePicture: person.profilePicture || ''
      });
      this.cdr.detectChanges();
      return;
    }

    this.friendshipService.sendFriendRequest(person.id).subscribe({
      next: (res: any) => {
        person.isPending = true;
        person.friendshipId = res?.friendshipId || res?.id;
        this.refreshAll();
      },
      error: (err) => console.error(err)
    });
  }

  cancelRequest(person: ExtendedMatch): void {
    if (person.isMock) {
      person.isPending = false;
      this.sentRequests = this.sentRequests.filter(r => r.Friendship_Id !== person.friendshipId);
      this.cdr.detectChanges();
      return;
    }

    if (person.friendshipId) {
      this.friendshipService.cancelFriendRequest(person.friendshipId).subscribe({
        next: () => {
          person.isPending = false;
          this.refreshAll();
        },
        error: (err) => console.error(err)
      });
    }
  }

  cancelSentRequestDirect(friendshipId: number): void {
    this.friendshipService.cancelFriendRequest(friendshipId).subscribe({
      next: () => {
        this.refreshAll();
      },
      error: (err) => {
        console.error(err);
        this.sentRequests = this.sentRequests.filter(r => r.Friendship_Id !== friendshipId);
        this.cdr.detectChanges();
      }
    });
  }

  acceptRequest(request: Friendship): void {
    this.friendshipService.acceptFriendRequest(request.Friendship_Id).subscribe({
      next: () => {
        this.refreshAll();
      },
      error: (err) => console.error(err)
    });
  }

  rejectRequest(request: Friendship): void {
    this.friendshipService.rejectFriendRequest(request.Friendship_Id).subscribe({
      next: () => {
        this.refreshAll();
      },
      error: (err) => console.error(err)
    });
  }
}
