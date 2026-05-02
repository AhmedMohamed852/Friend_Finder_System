package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.RolesDto;
import ahmed.com.springboot.friend_finder_system.eNum.RoleType;


public interface Role_Service {

    RolesDto getRole(RoleType role);
}
