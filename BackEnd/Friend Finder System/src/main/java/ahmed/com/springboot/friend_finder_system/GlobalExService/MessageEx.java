package ahmed.com.springboot.friend_finder_system.GlobalExService;

public interface MessageEx {

    static RuntimeException idRequired() {
        return new RuntimeException("error.must.be.not.null.argument.id");
    }

    static RuntimeException senderReceiverSame() {
        return new RuntimeException("error.message.sender.receiver.same");
    }

    static RuntimeException contentRequired() {
        return new RuntimeException("error.message.content.required");
    }

    static RuntimeException messageNotFound() {
        return new RuntimeException("error.message.not.found");
    }

    static RuntimeException unauthorized() {
        return new RuntimeException("error.message.unauthorized");
    }
}