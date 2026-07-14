package ahmed.com.springboot.friend_finder_system.GlobalExService;

public interface PaginationEx {

    static RuntimeException invalidPageNumber() {
        return new RuntimeException("page.number.invalid");
    }
}
