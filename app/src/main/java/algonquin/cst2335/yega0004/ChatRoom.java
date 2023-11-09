package algonquin.cst2335.yega0004;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.yega0004.Data.ChatRoomViewModel;
import algonquin.cst2335.yega0004.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.yega0004.databinding.RecieveMessageBinding;
import algonquin.cst2335.yega0004.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;
    ChatRoomViewModel chatModel ;

//    ArrayList<String> messages = new ArrayList<>();

    private ArrayList<ChatMessage> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if (messages == null ) {
        chatModel.messages.postValue(messages = new ArrayList<>());

        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button button = findViewById(R.id.button);
        binding.button.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.editText.getText().toString(), currentDateAndTime, true );

            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.editText.setText("");
        });
        Button button2 = findViewById(R.id.button2);
        binding.button2.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.editText.getText().toString(), currentDateAndTime, false );

            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.editText.setText("");
        });



        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding sentMessageBinding = SentMessageBinding.inflate(getLayoutInflater());
                    sentMessageBinding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    return new MyRowHolder(sentMessageBinding.getRoot());

                }else {
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
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
            public int getItemViewType(int position){
                if (messages.get(position).isSentButton()) {
                    return 0;
                } else {
                    return 1;
                }
            }


        });
    }
    class MyRowHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

        }
    }
}