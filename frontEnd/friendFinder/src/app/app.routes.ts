import { Routes } from '@angular/router';
import { authGuard } from './core/guards/guards';
import { guestGuard } from './core/guest/guest';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login').then(m => m.LoginComponent),
    canActivate: [guestGuard]
  },
  {
    path: 'updateProfile',
    loadComponent: () => import('./pages/user/updateProfile/update-profile/update-profile').then(m => m.UpdateProfile),
    canActivate: [authGuard]
  },
  {
    path: 'signup',
    loadComponent: () => import('./pages/sign-up/sign-up').then(m => m.SignupComponent),
    canActivate: [guestGuard]
  },
  {
    path: 'home',
    loadComponent: () => import('./pages/home/home').then(m => m.Home),
    canActivate: [authGuard]
  },
  {
    path: 'create-post',
    loadComponent: () => import('./pages/createPost/create-post/create-post').then(m => m.CreatePost),
    canActivate: [authGuard]
  },
  {
    path: 'profile/:id',
    loadComponent: () => import('./pages/user/profile').then(m => m.Profile),
    canActivate: [authGuard]
  },
  {
    path: 'my-friends',
    loadComponent: () => import('./pages/my-friends/my-friends').then(m => m.MyFriendsComponent),
    canActivate: [authGuard]
  },
  {
    path: 'post/:id',
    loadComponent: () => import('./pages/post-details/post-details/post-details').then(m => m.PostDetails),
    canActivate: [authGuard]
  },
  // ── Messages ─────────────────────────────────────────────
  {
    path: 'messages',
    loadComponent: () => import('./pages/Messages/messages/messages').then(m => m.MessagesComponent),
    canActivate: [authGuard]
  },
  {
    path: 'messages/:userId',   // فتح محادثة مباشرة مع شخص معين
    loadComponent: () => import('./pages/Messages/messages/messages').then(m => m.MessagesComponent),
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: 'home' }
];
