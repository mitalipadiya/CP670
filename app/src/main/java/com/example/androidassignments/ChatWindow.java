package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    ListView listView;
    EditText messageEditText;
    Button sendBtn;
    ArrayList<String> chatMessages;
    ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listView = findViewById(R.id.list_view);
        messageEditText = findViewById(R.id.message);
        sendBtn = findViewById(R.id.send_btn);
        chatMessages = new ArrayList<>();
        messageAdapter = new ChatAdapter( this );
        listView.setAdapter (messageAdapter);
    }
    public void onSendClick(View view) {
        String newMessage = String.valueOf(messageEditText.getText());
        chatMessages.add(newMessage);
        messageAdapter.notifyDataSetChanged();
        messageEditText.setText("");
    }
    private class ChatAdapter extends ArrayAdapter<String> {
        ChatAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return chatMessages.size();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return chatMessages.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }
    }
}