package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.GlobalExService.MessageEx;
import ahmed.com.springboot.friend_finder_system.dto.MessagesDto;
import ahmed.com.springboot.friend_finder_system.globalCurrentUserId.CurrentUser;
import ahmed.com.springboot.friend_finder_system.mapper.MessagesMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.models.Messages;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Messages_Repo;
import ahmed.com.springboot.friend_finder_system.service.Message_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessagesServiceImpl implements Message_Service {

    //TODO: Declare Service Methods

    private final Messages_Repo messages_Repo;
    private final MessagesMapper messagesMapper;
    private final User_Service userService;
    private final UserMapper userMapper;
    private final ResourceBundleMessageSource messageSource;


    //TODO:_______________ Implement Service Methods ____________________________


    //TODO:_______________ Send Message ____________________________
    @Override
    public MessagesDto sendMessage(Long receiverId, MessagesDto messagesDto) {

        if (receiverId == null) {
            throw MessageEx.idRequired();
        }

        Long senderId = CurrentUser.currentUserId();

        if (senderId.equals(receiverId)) {
            throw MessageEx.senderReceiverSame();
        }

        if (messagesDto.getContent() == null || messagesDto.getContent().isBlank()) {
            throw MessageEx.contentRequired();
        }

        User sender = userMapper.toEntity(userService.getUserById(senderId));
        User receiver = userMapper.toEntity(userService.getUserById(receiverId));

        Messages message = new Messages();
        message.setContent(messagesDto.getContent());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setIsRead(false);

        Messages saved = messages_Repo.save(message);

        String content = messageSource.getMessage("notification.message.received",
                new Object[]{sender.getFirstName(), sender.getLastName()},
                LocaleContextHolder.getLocale());

        return messagesMapper.toDto(saved);
    }


    //TODO:_______________ Get Message By Id ____________________________
    @Override
    public MessagesDto getMessageById(Long messageId) {

        if (messageId == null) {
            throw MessageEx.idRequired();
        }
        Messages message = messages_Repo.findById(messageId).orElseThrow(MessageEx::messageNotFound);

        return messagesMapper.toDto(message);
    }


    //TODO:_______________ Get Conversation ____________________________
    @Override
    public List<MessagesDto> getConversation(Long userId1, Long userId2) {

        if (userId1 == null || userId2 == null) {
            throw MessageEx.idRequired();
        }
        List<Messages> conversation = messages_Repo.findConversation(userId1, userId2);

        return conversation.stream().map(messagesMapper::toDto).toList();
    }




    //TODO:_______________ Get Inbox ____________________________
    @Override
    public List<MessagesDto> getInbox(Long userId) {

        if (userId == null) {
            throw  MessageEx.idRequired();
        }

        List<Messages> inbox = messages_Repo.findByReceiver_IdOrderByCreatedDateDesc(userId).orElse(List.of());

        return inbox.stream().map(messagesMapper::toDto).toList();
    }


    //TODO:_______________ Get Sent Messages ____________________________
    @Override
    public List<MessagesDto> getSentMessages(Long userId) {

        if (userId == null) {
            throw  MessageEx.idRequired();
        }

        List<Messages> sent = messages_Repo.findBySender_IdOrderByCreatedDateDesc(userId).orElse(List.of());

        return sent.stream().map(messagesMapper::toDto).toList();
    }


    //TODO:_______________ Get Unread Messages ____________________________
    @Override
    public List<MessagesDto> getUnreadMessages(Long userId) {

        if (userId == null) {
            throw MessageEx.idRequired();
        }

        List<Messages> unread = messages_Repo.findByReceiver_IdAndIsReadFalseOrderByCreatedDateDesc(userId);

        return unread.stream().map(messagesMapper::toDto).toList();
    }


    //TODO:_______________ Mark As Read ____________________________
    @Override
    public MessagesDto markAsRead(Long messageId) {

        if (messageId == null) {
            throw MessageEx.idRequired();
        }

        Messages message = messages_Repo.findById(messageId)
                .orElseThrow(MessageEx::messageNotFound);

        if (!message.getReceiver().getId().equals(CurrentUser.currentUserId())) {
            throw MessageEx.unauthorized();
        }
        message.setIsRead(true);

        return messagesMapper.toDto(messages_Repo.save(message));
    }


    //TODO:_______________ Mark Conversation As Read ____________________________
    @Override
    public void markConversationAsRead(Long senderId, Long receiverId) {

        if (senderId == null || receiverId == null) {
            throw MessageEx.idRequired();
        }

        List<Messages> unreadMessages =
                messages_Repo.findBySender_IdAndReceiver_IdAndIsReadFalse(senderId, receiverId);

        unreadMessages.forEach(m -> m.setIsRead(true));

        messages_Repo.saveAll(unreadMessages);
    }


    //TODO:_______________ Count Unread Messages ____________________________
    @Override
    public long countUnreadMessages(Long userId) {

        if (userId == null) {
            throw MessageEx.idRequired();
        }

        return messages_Repo.countByReceiver_IdAndIsReadFalse(userId);
    }


    //TODO:_______________ Update Message ____________________________
    @Override
    public MessagesDto updateMessage(Long messageId, MessagesDto messagesDto) {

        if (messageId == null) {
            throw MessageEx.idRequired();
        }

        Messages message = messages_Repo.findById(messageId)
                .orElseThrow(MessageEx::messageNotFound);

        if (!message.getSender().getId().equals(CurrentUser.currentUserId())) {
            throw MessageEx.unauthorized();
        }

        if (messagesDto.getContent() == null || messagesDto.getContent().isBlank()) {
            throw MessageEx.contentRequired();
        }
        message.setContent(messagesDto.getContent());

        return messagesMapper.toDto(messages_Repo.save(message));
    }


    //TODO:_______________ Delete Message ____________________________
    @Override
    public void deleteMessage(Long messageId) {

        if (messageId == null) {
            throw MessageEx.idRequired();
        }

        Messages message = messages_Repo.findById(messageId).orElseThrow(MessageEx::messageNotFound);

        if (!message.getSender().getId().equals(CurrentUser.currentUserId())) {
            throw MessageEx.unauthorized();
        }

        messages_Repo.delete(message);
    }

}