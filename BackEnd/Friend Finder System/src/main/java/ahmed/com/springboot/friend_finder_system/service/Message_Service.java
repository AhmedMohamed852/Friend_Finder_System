package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.MessagesDto;

import java.util.List;

public interface Message_Service {


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

}