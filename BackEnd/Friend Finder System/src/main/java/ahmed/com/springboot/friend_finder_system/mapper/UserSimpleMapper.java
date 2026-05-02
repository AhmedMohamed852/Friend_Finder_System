package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserSimpleMapper {

    User toEntity(User_Simple_Dto userDto);
    User_Simple_Dto toDto(User user);
}
