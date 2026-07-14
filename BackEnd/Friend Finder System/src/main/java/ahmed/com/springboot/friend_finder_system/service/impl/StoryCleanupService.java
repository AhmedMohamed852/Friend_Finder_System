package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.repo.Stories_Repo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StoryCleanupService {

    private final Stories_Repo storiesRepo;

    // كل دقيقة يفحص ويمسح أي ستوري عدى عليها 5 دقايق
    @Scheduled(fixedRate = 60000) // 60000ms = دقيقة واحدة
    @Transactional
    public void cleanupExpiredStories() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(10);
        storiesRepo.deleteExpiredStories(cutoff);
    }
}