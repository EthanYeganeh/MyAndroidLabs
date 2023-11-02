package algonquin.cst2335.yega0004;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import algonquin.cst2335.yega0004.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.yega0004.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<String> messages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(click -> {
            messages.add(binding.editText.getText().toString());

            binding.editText.setText("");
        });

        binding.recyclerView.setAdapter(new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.timeText.setText("");
                String obj = messages.get(position);
                holder.messageText.setText(obj);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
            public int getItemViewType(int position){
                return 0;
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