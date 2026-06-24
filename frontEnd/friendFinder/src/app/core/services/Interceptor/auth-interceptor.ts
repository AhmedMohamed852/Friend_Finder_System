import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // متضفش Authorization على الطلبات اللي رايحة لخدمات خارجية زي imgbb
  if (req.url.includes('imgbb.com')) {
    return next(req);
  }

  const authService = inject(AuthService);
  const token = authService.getToken();

  // طباعة لمراقبة خروج التوكن في الـ Console عندك
  console.log('🚀 Interceptor triggering for URL:', req.url);
  console.log('🔑 Token found status:', token ? 'Yes (Sending...)' : 'No (Empty)');

  // لو التوكن موجود، بنعمل نسخة معدلة من الريكويست ونضيف الهيدر
  if (token) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedReq);
  }

  // لو مش موجود (زي في صفحة اللوجين) بيمرر الريكويست العادي
  return next(req);
};
