package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.MessagesDto;

import java.util.List;

public interface MessagesService {

    /**
     * Send a new message from one user to another.
     * @param senderId   ID of the sender
     * @param receiverId ID of the receiver
     * @param messagesDto message payload (content)
     * @return the created message as DTO
     */
    MessagesDto sendMessage(Long senderId, Long receiverId, MessagesDto messagesDto);

    /**
     * Get a single message by its ID.
     */
    MessagesDto getMessageById(Long messageId);

    /**
     * Get the full conversation between two users, ordered by creation date.
     */
    List<MessagesDto> getConversation(Long userId1, Long userId2);

    /**
     * Get all messages received by a specific user (inbox).
     */
    List<MessagesDto> getInbox(Long userId);

    /**
     * Get all messages sent by a specific user.
     */
    List<MessagesDto> getSentMessages(Long userId);

    /**
     * Get all unread messages for a specific user.
     */
    List<MessagesDto> getUnreadMessages(Long userId);

    List<User_Simple_Dto> search (String key);

    /**
     * Mark a single message as read.
     */
    MessagesDto markAsRead(Long messageId);

    /**
     * Mark all messages in a conversation (from one user to another) as read.
     */
    void markConversationAsRead(Long senderId, Long receiverId);

    /**
     * Count unread messages for a user (useful for notification badges).
     */
    long countUnreadMessages(Long userId);

    /**
     * Update the content of an existing message.
     */
    MessagesDto updateMessage(Long messageId, MessagesDto messagesDto);

    /**
     * Delete a message by ID.
     */
    void deleteMessage(Long messageId);
}