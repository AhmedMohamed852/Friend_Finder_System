package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.InterestsDto;
import ahmed.com.springboot.friend_finder_system.models.Interests;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InterestsMapper {

    Interests toEntity(InterestsDto interestsDto);

    InterestsDto toDto(Interests interests);

    List<Interests> toEntityList(List<InterestsDto> interestsDto);

    List<InterestsDto> toDtoList(List<Interests> interests);
}
