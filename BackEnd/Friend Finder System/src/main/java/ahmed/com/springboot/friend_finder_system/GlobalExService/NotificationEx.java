package ahmed.com.springboot.friend_finder_system.GlobalExService;

public interface NotificationEx {

    static RuntimeException userIdRequired() {
        return new RuntimeException("error.must.be.not.null.argument.id");
    }

    static RuntimeException notificationAlreadyExists() {
        return new RuntimeException("error.this.notification.exist");
    }

    static RuntimeException notificationNotFound() {
        return new RuntimeException("error.this.notification.not.found");
    }
}