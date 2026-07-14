package ahmed.com.springboot.friend_finder_system.globalCurrentUserId;

import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;

public interface CurrentUser {

    static Long currentUserId()
    {
        UserDto currentUser = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        return userId;
    }
}
