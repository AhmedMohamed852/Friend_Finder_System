package ahmed.com.springboot.friend_finder_system.GlobalExService;


public interface UserEx {

    // ── Register ──────────────────────────────────────────
    static RuntimeException usernameAlreadyExists() {
        return new RuntimeException("error.username.already.exists");
    }

    static RuntimeException emailAlreadyExists() {
        return new RuntimeException("error.email.already.exists");
    }

    // ── Profile / Get / Update / Delete ──────────────────
    static RuntimeException userNotFound() {
        return new RuntimeException("error.user.not.found");
    }

    static RuntimeException userIdRequired() {
        return new RuntimeException("error.user.id.is.required");
    }

    // ── Search ────────────────────────────────────────────
    static RuntimeException searchKeyRequired() {
        return new RuntimeException("error.user.id.not.found");
    }

    static RuntimeException noUsersFoundForSearch() {
        return new RuntimeException("error.user.id.not.found");
    }

    // ── Pagination ────────────────────────────────────────
    static RuntimeException invalidPageNumber() {
        return new RuntimeException("page.number.invalid");
    }
}