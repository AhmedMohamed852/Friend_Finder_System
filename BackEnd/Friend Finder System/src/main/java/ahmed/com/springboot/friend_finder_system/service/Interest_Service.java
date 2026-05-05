package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.InterestsDto;

import java.util.List;

public interface Interest_Service {

    List<InterestsDto> getAllInterests();

    void setListInterests(List<InterestsDto> interestsDto , Long id);

    List<InterestsDto> getInterestsById(Long id);


}
