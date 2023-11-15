    package algonquin.cst2335.yega0004;

    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.PrimaryKey;


    @Entity
    public class ChatMessage {


        @ColumnInfo(name="message")
        public String message;

        @ColumnInfo(name="TimeSent")
        public String timeSent;

        @ColumnInfo(name="IsSentButton")
        public boolean isSentButton;

        @PrimaryKey (autoGenerate = true)
        @ColumnInfo(name="id")
        public int id;


        public ChatMessage() {
        }
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
