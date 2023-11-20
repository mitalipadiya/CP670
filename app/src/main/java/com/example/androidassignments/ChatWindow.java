package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidassignments.utils.ChatDatabaseHelper;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    private static final int DELETE_MESSAGE_REQUEST = 1;

    private static final String ACTIVITY_NAME = "ChatWindow";
    ListView listView;
    EditText messageEditText;
    Button sendBtn;
    static ArrayList<String> chatMessages;
    ChatAdapter messageAdapter;
    SQLiteDatabase database;
    private boolean isTablet;
    ChatDatabaseHelper databaseHelper;
    static Cursor cursor;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listView = findViewById(R.id.list_view);
        messageEditText = findViewById(R.id.message);
        sendBtn = findViewById(R.id.send_btn);
        chatMessages = new ArrayList<>();

        FrameLayout frameLayout = findViewById(R.id.new_frame_layout);
        isTablet = frameLayout != null;
        messageAdapter = new ChatAdapter( this);

        listView.setAdapter (messageAdapter);
        // Set item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected message ID
                long selectedMessageId = messageAdapter.getItemId(position);

                Bundle bundle = new Bundle();
                bundle.putLong("message_id", selectedMessageId);
                bundle.putString("message_text", chatMessages.get(position));
                // Check if the device is a tablet
                if (isTablet) {
                    // Tablet: Use FragmentTransaction to show details in a fragment
                    MessageDetailsFragment fragment = new MessageDetailsFragment(ChatWindow.this);
                    fragment.setArguments(bundle);

                    // Begin FragmentTransaction
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.new_frame_layout, fragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    // Phone layout: Start MessageDetailsActivity
                    Intent intent = new Intent(ChatWindow.this, MessageDetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, DELETE_MESSAGE_REQUEST);
                }
            }
        });
        fetchData();

    }
    void fetchData () {
        databaseHelper = new ChatDatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();
        cursor = database.rawQuery("SELECT " + ChatDatabaseHelper.KEY_ID + ", " +
                ChatDatabaseHelper.KEY_MESSAGE +
                " FROM " + ChatDatabaseHelper.TABLE_NAME, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor != null) {
                int messageID = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
                String message = cursor.getString(messageID);
                chatMessages.add(message);
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
                cursor.moveToNext();
            }
        }
        if(messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
        }
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column Name: " + cursor.getColumnName(i));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Check if the result is from MessageDetails activity
            if (requestCode == DELETE_MESSAGE_REQUEST) {
                // Check if the data contains messageIdToDelete
                if (data != null && data.hasExtra("deleted_message_id")) {
                    long messageIdToDelete = data.getLongExtra("deleted_message_id", -1);

                    // Handle deletion logic
                    deleteMessage(messageIdToDelete);
                }
            }
        }
    }
    public void deleteMessage(long messageId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Define the WHERE clause to match the message ID
        String selection = ChatDatabaseHelper.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(messageId)};

        // Delete the message
        db.delete(ChatDatabaseHelper.TABLE_NAME, selection, selectionArgs);
        updateListView();

    }
    private void updateListView() {
        chatMessages.clear();
        fetchData();
        if(messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
        }
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
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (database != null) {
            database.close();
        }
    }
    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(@NonNull Context context) {
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
        @Override
        public long getItemId(int position) {
            cursor = database.rawQuery("SELECT " + ChatDatabaseHelper.KEY_ID + ", " +
                    ChatDatabaseHelper.KEY_MESSAGE +
                    " FROM " + ChatDatabaseHelper.TABLE_NAME, null);
            if (cursor != null && cursor.moveToPosition(position)) {
                int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID);
                return cursor.getLong(messageIndex);
            }
            return -1;
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