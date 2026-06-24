import { Component, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { LeftBarComponent } from './layout/left-bar/left-bar';
import { RightBarComponent } from './layout/right-bar/right-bar';
import { Footer } from './layout/footer/footer';
import { Header } from './layout/header/header';
import { AuthService } from './core/services/auth/auth';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    LeftBarComponent,
    RightBarComponent,
    Footer,
    Header
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {
  private authService = inject(AuthService);
  public isLoggedIn = computed(() => this.authService.isLoggedIn());
}
