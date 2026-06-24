import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth/auth';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {

  private authService = inject(AuthService);
  private router = inject(Router);

  public isLoggedIn = computed(() => this.authService.isLoggedIn());

  public onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
