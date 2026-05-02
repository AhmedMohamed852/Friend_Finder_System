package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    List<User> toEntityList(List<UserDto> userDto);

    List<UserDto> toDtoList(List<User> users);
}
