package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.MessagesDto;

import java.util.List;

public interface Message_Service {

    void sendMessage( Long receiverId, String message); // (senderId, receiverId, message)

    List<MessagesDto> getConversation(Long receiverId); // (senderId, receiverId)

    void deleteMessage(Long messageId);


}
