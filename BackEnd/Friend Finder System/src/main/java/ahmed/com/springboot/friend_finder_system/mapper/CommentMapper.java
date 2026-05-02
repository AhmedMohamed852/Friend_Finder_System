package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.CommentDto;
import ahmed.com.springboot.friend_finder_system.models.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    Comments toEntity(CommentDto commentDto);

    CommentDto toDto(Comments comment);

    List<Comments> toEntityList(List<CommentDto> commentDto);

    List<CommentDto> toDtoList(List<Comments> comments);
}
