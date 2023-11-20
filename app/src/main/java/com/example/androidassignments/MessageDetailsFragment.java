package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class MessageDetailsFragment extends Fragment {
    private ChatWindow chatWindow;

    public MessageDetailsFragment() {
        // Required empty public constructor
    }

    public MessageDetailsFragment(ChatWindow chatWindow) {
        this.chatWindow = chatWindow;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_details, container, false);

        // Retrieve data from the Bundle
        Bundle args = getArguments();
        if (args != null) {
            long messageId = args.getLong("message_id", 0);
            String messageText = args.getString("message_text", "");

            // Use the data as needed
            TextView messageIdTextView = view.findViewById(R.id.idTextView);
            TextView messageTextView = view.findViewById(R.id.messageTextView);
            Button deleteButton = view.findViewById(R.id.deleteButton);

            messageIdTextView.setText("Message ID: " + messageId);
            messageTextView.setText("Message Text: " + messageText);

            // Set OnClickListener for the delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(chatWindow == null ) {
                        // Notify the calling activity about the deletion
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("deleted_message_id", messageId);
                        getActivity().setResult(Activity.RESULT_OK, resultIntent);

                        // Finish the current activity
                        getActivity().finish();
                    } else {
                        // Notify the calling activity about the deletion
                        if (chatWindow != null) {
                            chatWindow.deleteMessage(messageId);
                        }
                        getParentFragmentManager().beginTransaction().remove(MessageDetailsFragment.this).commit();
                    }
                }
            });
        }

        return view;
    }
}
