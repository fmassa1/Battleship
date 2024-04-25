import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 42L;

    private String message;

    public Message(String message) {
        this.message = checkIfAppropriate(message);
    }
    private String checkIfAppropriate(String message) {
        return message;
    }
    public void getAI() {

    }
    public String getMessage() {
        return message;
    }
}
