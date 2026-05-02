package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.dto.RolesDto;
import ahmed.com.springboot.friend_finder_system.eNum.RoleType;
import ahmed.com.springboot.friend_finder_system.mapper.RolesMapper;
import ahmed.com.springboot.friend_finder_system.models.Roles;
import ahmed.com.springboot.friend_finder_system.repo.Roles_Repo;
import ahmed.com.springboot.friend_finder_system.service.Role_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class Role_Service_Impl implements Role_Service {

    private final Roles_Repo roles_Repo;
    private final RolesMapper rolesMapper;




    @Override
    public RolesDto getRole(RoleType role)
    {
        Roles roles = roles_Repo.findByName((role));
        if(roles == null)
        {
            throw new RuntimeException("error.role.not.found");
        }
        return rolesMapper.toDto(roles);
    }
}
