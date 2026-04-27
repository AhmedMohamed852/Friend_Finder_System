package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.CommentDto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import org.apache.catalina.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ap.shaded.freemarker.core.Comment;

import java.util.List;

@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    Comment toEntity(CommentDto commentDto);

    CommentDto toDto(Comment comment);

    List<Comment> toEntityList(List<CommentDto> commentDto);

    List<CommentDto> toDtoList(List<Comment> comments);
}
