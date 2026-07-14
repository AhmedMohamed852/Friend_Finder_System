package ahmed.com.springboot.friend_finder_system.GlobalExService;

/**
 * مكان مركزي لكل الـ Exceptions الخاصة بالـ Stories Service.
 * الاستخدام:
 *   throw StoriesEx.storyDataRequired();
 */
public interface StoriesEx {


    static RuntimeException storyNotFound() {
        return new RuntimeException("error.story.not.found");
    }


    static RuntimeException storyUploadFailed() {
        return new RuntimeException("error.story.upload.failed");
    }

}