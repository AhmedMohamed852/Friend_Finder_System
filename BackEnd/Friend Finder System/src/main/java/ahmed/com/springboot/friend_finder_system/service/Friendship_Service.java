package ahmed.com.springboot.friend_finder_system.service;


public interface Friendship_Service {



    void sendFriendRequest(Long friendId);

    void acceptFriendRequest(Long Friendship_Id);

    void rejectFriendRequest(Long Friendship_Id);

    void cancelFriendRequest(Long Friendship_Id);

    void unfriend(Long friendId);

    void blockUser(Long friendId);




}
