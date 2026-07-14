package ahmed.com.springboot.friend_finder_system.GlobalExService;

public interface PostsEx {

    static RuntimeException postIdRequired() {
        return new RuntimeException("error.post.id.is.required");
    }

    static RuntimeException postNotFound() {
        return new RuntimeException("error.post.not.found");
    }

    static RuntimeException noPostsFoundForUser() {
        return new RuntimeException("error.user.id.not.found");
    }
}