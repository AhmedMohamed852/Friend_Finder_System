package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.MessagesDto;
import ahmed.com.springboot.friend_finder_system.models.Messages;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessagesMapper {

    Messages toEntity(MessagesDto messagesDto);

    MessagesDto toDto(Messages messages);

    List<Messages> toEntityList(List<MessagesDto> messagesDto);

    List<MessagesDto> toDtoList(List<Messages> messages);
}
