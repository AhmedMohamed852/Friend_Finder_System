package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.StoriesDto;

import java.util.List;

public interface Stories_Service {

    void newStory (StoriesDto storiesDto);
    List<StoriesDto> getStories ();
}
