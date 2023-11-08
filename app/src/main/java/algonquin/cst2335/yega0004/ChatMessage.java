package algonquin.cst2335.yega0004;public class ChatMessage {

    public String message;
    public String timeSent;
    public boolean isSentButton;

    public ChatMessage (String m, String t, boolean sent)
    {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}
