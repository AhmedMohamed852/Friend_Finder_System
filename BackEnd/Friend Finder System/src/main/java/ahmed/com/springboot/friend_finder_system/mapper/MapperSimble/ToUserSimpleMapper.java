package ahmed.com.springboot.friend_finder_system.mapper.MapperSimble;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ToUserSimpleMapper {

    User_Simple_Dto toUserSimpleDto(User user);
    User toUser(User_Simple_Dto userDto);

    List<User_Simple_Dto> toUserSimpleDto(List<User> users);
    List<User> toUser(List<User_Simple_Dto> userDto);

}

