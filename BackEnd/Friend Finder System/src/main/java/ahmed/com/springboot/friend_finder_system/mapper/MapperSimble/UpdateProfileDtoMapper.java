package ahmed.com.springboot.friend_finder_system.mapper.MapperSimble;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.UpdateProfileDto;
import ahmed.com.springboot.friend_finder_system.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UpdateProfileDtoMapper {

    User toEntity(UpdateProfileDto updateProfileDto);
}
