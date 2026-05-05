package ahmed.com.springboot.friend_finder_system.mapper;


import ahmed.com.springboot.friend_finder_system.dto.MediaDto;
import ahmed.com.springboot.friend_finder_system.models.Media;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MediaMapper {

    Media toEntity(MediaDto mediaDto);

    MediaDto toDto(Media media);

    Set<Media> toEntityList(List<MediaDto> mediaDto);

    Set<MediaDto> toDtoList(List<Media> media);
}
