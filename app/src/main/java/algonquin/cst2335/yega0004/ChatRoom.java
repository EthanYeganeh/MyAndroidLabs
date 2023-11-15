/**
 * This class represents the ChatRoom activity, where users can send and receive chat messages.
 */
package algonquin.cst2335.yega0004;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.yega0004.Data.ChatRoomViewModel;
import algonquin.cst2335.yega0004.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.yega0004.databinding.RecieveMessageBinding;
import algonquin.cst2335.yega0004.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    // View binding for the activity
    ActivityChatRoomBinding binding;

    // Adapter for the RecyclerView
    private RecyclerView.Adapter myAdapter;

    // ViewModel for managing the chat messages
    ChatRoomViewModel chatModel;

    // Data Access Object for interacting with the database
    private ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Room database
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MessageDatabase").build();
        mDAO = db.cmDAO();
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        // Inflate the layout using ViewBinding
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Clear messages when the activity is created
        chatModel.messages.setValue(new ArrayList<>());

        // Set up the RecyclerView with a LinearLayoutManager
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the "Send" button click listener
        Button button = findViewById(R.id.button);
        binding.button.setOnClickListener(click -> {
            // Get the current date and time
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());

            // Create a new chat message and add it to the list
            ChatMessage chatMessage = new ChatMessage(binding.editText.getText().toString(), currentDateAndTime, true);
            chatModel.messages.getValue().add(chatMessage);
            myAdapter.notifyItemInserted(chatModel.messages.getValue().size() - 1);
            binding.editText.setText("");

            // Insert the message into the database using a background thread
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> mDAO.insertMessage(chatMessage));
        });

        // Set up the "Receive" button click listener
        Button button2 = findViewById(R.id.button2);
        binding.button2.setOnClickListener(click -> {
            // Get the current date and time
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());

            // Create a new chat message and add it to the list
            ChatMessage chatMessage = new ChatMessage(binding.editText.getText().toString(), currentDateAndTime, false);
            chatModel.messages.getValue().add(chatMessage);
            myAdapter.notifyItemInserted(chatModel.messages.getValue().size() - 1);
            binding.editText.setText("");

            // Insert the message into the database using a background thread
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> mDAO.insertMessage(chatMessage));
        });

        // Set up the RecyclerView adapter
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a ViewHolder based on the message type (sent or received)
                if (viewType == 0) {
                    SentMessageBinding sentMessageBinding = SentMessageBinding.inflate(getLayoutInflater());
                    sentMessageBinding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    return new MyRowHolder(sentMessageBinding.getRoot());
                } else {
                    RecieveMessageBinding receiveMessageBinding = RecieveMessageBinding.inflate(getLayoutInflater());
                    receiveMessageBinding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    return new MyRowHolder(receiveMessageBinding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                // Bind the data to the ViewHolder based on the position
                ChatMessage obj = chatModel.messages.getValue().get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                // Return the total number of messages in the list
                return chatModel.messages.getValue().size();
            }

            @Override
            public int getItemViewType(int position) {
                // Return the view type based on the message type (sent or received)
                if (chatModel.messages.getValue().get(position).isSentButton()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }

    /**
     * This inner class represents a ViewHolder for each row in the RecyclerView.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {

        // TextViews for displaying message text and time
        TextView messageText;
        TextView timeText;

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The View representing each row in the RecyclerView.
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            // Set up click listener for each row to handle message deletion
            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage clickedMessage = chatModel.messages.getValue().get(position);

                // Show a confirmation dialog for message deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete this message? " + clickedMessage.getMessage())
                        .setTitle("Question:")
                        .setNegativeButton("No", (dialog, cl) -> {
                        })
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            // Delete the message and show a Snackbar with undo option
                            ChatMessage removedMessage = chatModel.messages.getValue().get(position);
                            chatModel.messages.getValue().remove(position);
                            myAdapter.notifyItemRemoved(position);
                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG).setAction("Undo", Clk -> {
                                chatModel.messages.getValue().add(position, removedMessage);
                                myAdapter.notifyItemInserted(position);
                            }).show();
                        }).create().show();
            });

            // Initialize TextViews for message text and time
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}
