package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.eNum.InterestCategory;
import ahmed.com.springboot.friend_finder_system.eNum.RoleType;
import ahmed.com.springboot.friend_finder_system.models.Interests;
import ahmed.com.springboot.friend_finder_system.models.Roles;
import ahmed.com.springboot.friend_finder_system.repo.Interests_Repo;
import ahmed.com.springboot.friend_finder_system.repo.Roles_Repo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class DataSeeder implements ApplicationRunner {

    private final Roles_Repo roles_Repo;
    private final Interests_Repo interests_Repo;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        setRoles();
        setInterests();
    }

    private void setRoles()
    {
        List<String> roles = List.of("ADMIN","USER");

        roles.forEach(roleName ->
        {
            if(!roles_Repo.existsByName(RoleType.valueOf(roleName)))
            {
                Roles Role = new Roles();
                Role.setName(RoleType.valueOf(roleName));
                roles_Repo.save(Role);
                log.info("✅ Role saved",roleName);
            }else {
                log.info("⏩Role already exists");
            }
        });
    }

    private void setInterests() {
        List<InterestCategory> interests = List.of(InterestCategory.values());

        interests.forEach(interest -> {
            if (!interests_Repo.existsByCategory(interest)) {

                Interests newInterest = new Interests();
                newInterest.setCategory(interest);

                interests_Repo.save(newInterest);
                log.info("✅ Interest saved: {}", interest);

            } else {
                log.info("⏩ Interest already exists: {}", interest);
            }
        });
    }
}
