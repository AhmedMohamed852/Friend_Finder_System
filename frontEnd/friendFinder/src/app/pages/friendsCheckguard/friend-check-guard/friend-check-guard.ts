import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map, catchError, of } from 'rxjs';
import {FriendshipService} from '../../../core/services/Friendship/friendship-service';

export const friendsCheckGuard: CanActivateFn = (route, state) => {
  const friendshipService = inject(FriendshipService);
  const router = inject(Router);

  return friendshipService.hasAnyFriends().pipe(
    map((hasFriends) => {
      if (hasFriends) {
        // عنده أصدقاء -> اسمح بفتح الصفحة
        return true;
      } else {
        // معندوش أصدقاء -> وجهه لصفحة الـ Onboarding
        router.navigate(['/welcome']); // اضبط المسار المخصص للمكون
        return false;
      }
    }),
    catchError(() => {
      // في حالة حدوث خطأ بالسيرفر، افتح الصفحة كبديل افتراضي
      return of(true);
    })
  );
};
