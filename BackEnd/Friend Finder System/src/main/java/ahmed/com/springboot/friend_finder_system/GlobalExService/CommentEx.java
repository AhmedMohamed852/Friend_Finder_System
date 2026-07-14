package ahmed.com.springboot.friend_finder_system.GlobalExService;

public interface CommentEx {

    static RuntimeException commentIdRequired() {
        return new RuntimeException("error.comment.id.is.required");
    }

    static RuntimeException commentNotFound() {
        return new RuntimeException("error.comment.not.found");
    }

    static RuntimeException unauthorizedDelete() {
        return new RuntimeException("error.comment.unauthorized.delete");
    }
}
