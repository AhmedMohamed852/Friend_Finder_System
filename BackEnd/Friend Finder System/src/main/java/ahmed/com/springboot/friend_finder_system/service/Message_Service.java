package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.MessagesDto;

import java.util.List;

public interface Message_Service {

    /**
     * Send a new message from the currently logged-in user to another user.
     * @param receiverId ID of the receiver
     * @param messagesDto message payload (content)
     * @return the created message as DTO
     */
    MessagesDto sendMessage(Long receiverId, MessagesDto messagesDto);

    MessagesDto getMessageById(Long messageId);

    List<MessagesDto> getConversation(Long userId1, Long userId2);

    List<MessagesDto> getInbox(Long userId);

    List<MessagesDto> getSentMessages(Long userId);

    List<MessagesDto> getUnreadMessages(Long userId);

    MessagesDto markAsRead(Long messageId);

    void markConversationAsRead(Long senderId, Long receiverId);

    long countUnreadMessages(Long userId);

    MessagesDto updateMessage(Long messageId, MessagesDto messagesDto);

    void deleteMessage(Long messageId);

   // List<User_Simple_Dto> search (String key);

}