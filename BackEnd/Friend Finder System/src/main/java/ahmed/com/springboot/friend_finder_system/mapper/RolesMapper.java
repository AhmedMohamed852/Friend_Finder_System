package ahmed.com.springboot.friend_finder_system.mapper;

import ahmed.com.springboot.friend_finder_system.dto.RolesDto;
import ahmed.com.springboot.friend_finder_system.models.Roles;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"  , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolesMapper {


    Roles toEntity(RolesDto rolesDto);

    RolesDto toDto(Roles roles);

    List<Roles> toEntityList(List<RolesDto> rolesDtoList);

    List<RolesDto> toDtoList(List<Roles> rolesList);
}
