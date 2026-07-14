package ahmed.com.springboot.friend_finder_system.GlobalExService;

public interface InterestsEx {

    static RuntimeException InterestsNotFoundException() {
        return new RuntimeException("error.interests.not.found");
    }
}
