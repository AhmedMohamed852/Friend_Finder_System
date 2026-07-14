package ahmed.com.springboot.friend_finder_system.GlobalExService;

public interface FriendshipEx {

    static RuntimeException alreadyExists() {
        return new RuntimeException("error.friendship.already.exists");
    }

    static RuntimeException notExists() {
        return new RuntimeException("error.friendships.not.exists");
    }

    static RuntimeException searchKeyRequired() {
        return new RuntimeException("error.user.id.not.found");
    }
}
