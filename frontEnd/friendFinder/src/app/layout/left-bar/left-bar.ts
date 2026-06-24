import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import { UserService, SimpleUserProfile } from '../../core/services/user/user-service';

@Component({
  selector: 'app-left-bar',
  standalone: true,
  imports: [CommonModule, RouterLinkActive, RouterLink],
  templateUrl: './left-bar.html',
  styleUrl: './left-bar.css'
})
export class LeftBarComponent implements OnInit {
  private userService = inject(UserService);
  private router = inject(Router);

  profile: SimpleUserProfile | null = null;
  loading = false;

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.loading = true;
    this.userService.getSimpleProfile().subscribe({
      next: (data) => {
        this.profile = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load simple profile:', err);
        this.loading = false;
      }
    });
  }

  goToMyProfile(): void {
    if (this.profile) {
      this.router.navigate(['/profile', this.profile.id]);
    }
  }
}
