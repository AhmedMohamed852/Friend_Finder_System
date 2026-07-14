package ahmed.com.springboot.friend_finder_system.GlobalExService;

public interface RoleEx {

    static RuntimeException roleNotFound() {
        return new RuntimeException("error.role.not.found");
    }
}
