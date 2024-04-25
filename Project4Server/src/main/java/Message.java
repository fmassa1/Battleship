// Ricky Massa
// 04-25-2024
// UIC Spring 2024
// Message.java
// class that is used for sending messages between clients

import java.io.Serializable;
import java.util.Random;

public class Message implements Serializable {
    private static final long serialVersionUID = 42L;

    private String message;

    public Message(String message) {
        this.message = checkIfAppropriate(message);
    }
    //checks if message has bad words and changes it if it does.
    private String checkIfAppropriate(String message) {
        String[] positivePhrases = {
                "You're amazing!",
                "You can do it!",
                "Believe in yourself!",
                "You're capable of great things!",
                "Keep up the good work!",
                "You're doing fantastic!",
                "You're making progress!",
                "You're on the right track!",
                "You've got this!",
                "You're a star!",
                "You're an inspiration!",
                "You're unstoppable!",
                "You're making a difference!",
                "You're one in a million!",
                "You're a winner!",
                "You're a champion!",
                "You're a shining light!",
                "You're full of potential!",
                "You're a rockstar!",
                "You're awesome!"
        };
        if(message.contains("shit") || message.contains("ass") || message.contains("damn") || message.contains("dumb") ||
                message.contains("fuck") || message.contains("suck") || message.contains("bad") || message.contains("butt") ||
                message.contains("stupid") || message.contains("idiot") || message.contains("poop") || message.contains("dick") ) {
            Random random = new Random();
            return positivePhrases[random.nextInt(positivePhrases.length)];
        }
        return message;
    }
    //sends a chat to players playing AI
    public void getAI() {
        String[] competitiveMessages = {
                "I hope you're ready to lose!",
                "Prepare to be defeated!",
                "You're going down!",
                "You'll never beat me!",
                "You're no match for my skills!",
                "I'm the best, and you're about to find out!",
                "You're just delaying the inevitable!",
                "I'll crush you!",
                "You think you can win? Think again!",
                "I thrive under pressure, unlike you!",
                "Victory is mine, and there's nothing you can do about it!",
                "You're out of your league!",
                "I'm not here to make friends; I'm here to win!",
                "You're nothing but a speed bump on my road to victory!",
                "I'll leave you in the dust!",
                "You're just a stepping stone in my path to greatness!",
                "I eat competitors like you for breakfast!",
                "You're just another opponent to be defeated!",
                "You may as well give up now!",
                "You'll regret challenging me!"
        };
        Random random = new Random();
        this.message = competitiveMessages[random.nextInt(competitiveMessages.length)];
    }
    public String getMessage() {
        return message;
    }
}
