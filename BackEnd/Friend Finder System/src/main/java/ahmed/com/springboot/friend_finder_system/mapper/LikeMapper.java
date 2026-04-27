package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.LikeDto;
import ahmed.com.springboot.friend_finder_system.models.Like;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {

    Like toEntity (LikeDto likeDto);

    LikeDto toDto(Like like);

    List<Like> toEntityList(List<LikeDto> likeDto);

    List<LikeDto> toDtoList(List<Like> likes);
}
