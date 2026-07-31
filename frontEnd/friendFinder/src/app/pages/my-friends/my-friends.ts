import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Friendship, FriendshipService } from '../../core/services/Friendship/friendship-service';

@Component({
  selector: 'app-my-friends',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-friends.html',
  styleUrl: './my-friends.css'
})
export class MyFriendsComponent implements OnInit {
  private friendshipService = inject(FriendshipService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  friends: Friendship[] = [];
  loading = false;

  ngOnInit(): void {
    this.loadFriends();
  }

  loadFriends(): void {
    this.loading = true;
    this.friendshipService.getMyFriends().subscribe({
      next: (data) => {
        this.friends = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loading = false;
        console.error('Error fetching friends:', err);
        this.cdr.detectChanges();
      }
    });
  }

  viewProfile(id: number): void {
    this.router.navigate(['/profile', id]);
  }
}
