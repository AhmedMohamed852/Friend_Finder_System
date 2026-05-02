package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.FriendshipDto;
import ahmed.com.springboot.friend_finder_system.models.Friendship;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

//@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FriendshipMapper {

    Friendship toEntity(FriendshipDto friendshipDto);

    FriendshipDto toDto(Friendship friendship);

    List<Friendship> toEntityList(List<FriendshipDto> friendshipDto);

    List<FriendshipDto> toDtoList(List<Friendship> friendships);
}
