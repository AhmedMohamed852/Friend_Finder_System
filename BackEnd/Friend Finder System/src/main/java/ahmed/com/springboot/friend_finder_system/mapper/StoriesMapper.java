package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.StoriesDto;
import ahmed.com.springboot.friend_finder_system.models.Stories;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoriesMapper {

    Stories toEntity(StoriesDto storiesDto);

    StoriesDto toDto(Stories stories);

    List<StoriesDto> toDtoList(List<Stories> stories);
}
