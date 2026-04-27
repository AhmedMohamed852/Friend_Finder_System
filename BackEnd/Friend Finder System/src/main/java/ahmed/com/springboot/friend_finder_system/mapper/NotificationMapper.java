package ahmed.com.springboot.friend_finder_system.mapper;


import ahmed.com.springboot.friend_finder_system.dto.NotificationDto;
import ahmed.com.springboot.friend_finder_system.models.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    Notification toEntity (NotificationDto notificationDto);

    NotificationDto toDto (Notification notification);

    List<Notification> toEntityList (List<NotificationDto> notificationDto);

    List<NotificationDto> toDtoList (List<Notification> notification);

}
