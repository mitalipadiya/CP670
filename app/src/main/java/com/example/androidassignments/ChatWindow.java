package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidassignments.utils.ChatDatabaseHelper;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "ChatWindow";
    ListView listView;
    EditText messageEditText;
    Button sendBtn;
    ArrayList<String> chatMessages;
    ChatAdapter messageAdapter;
    SQLiteDatabase database;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listView = findViewById(R.id.list_view);
        messageEditText = findViewById(R.id.message);
        sendBtn = findViewById(R.id.send_btn);
        chatMessages = new ArrayList<>();
        messageAdapter = new ChatAdapter( this );

        ChatDatabaseHelper databaseHelper = new ChatDatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor != null) {
                String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                chatMessages.add(message);
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
                cursor.moveToNext();

            }
        }

        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column Name: " + cursor.getColumnName(i));
        }

        listView.setAdapter (messageAdapter);
    }
    public void onSendClick(View view) {
        String newMessage = String.valueOf(messageEditText.getText());
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, newMessage);
        long newRowId = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        if (newRowId != -1) {
            Log.i(ACTIVITY_NAME, "Inserted a new message with ID: " + newRowId);
            chatMessages.add(newMessage);
            messageAdapter.notifyDataSetChanged();
            messageEditText.setText("");
        } else {
            Log.e(ACTIVITY_NAME, "Failed to insert a new message.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
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