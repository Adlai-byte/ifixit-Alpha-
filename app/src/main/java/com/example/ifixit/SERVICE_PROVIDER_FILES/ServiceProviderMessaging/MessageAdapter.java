package com.example.ifixit.SERVICE_PROVIDER_FILES.ServiceProviderMessaging;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context mContext;
    private String mCurrentUserUid;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
        mContext = context;
        mCurrentUserUid  = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.service_provider_message_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.messageText = convertView.findViewById(R.id.message_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Message message = getItem(position);
        viewHolder.messageText.setText(message.getMessageText());

        if (message.getSenderUid().equals(mCurrentUserUid)) {
            // Message sent by the current user
            viewHolder.messageText.setBackgroundResource(R.drawable.outgoing_message_bg);
            viewHolder.messageText.setTextColor(Color.WHITE);
            ((LinearLayout.LayoutParams) viewHolder.messageText.getLayoutParams()).gravity = Gravity.END;
        } else {
            // Message received from the remote user
            viewHolder.messageText.setBackgroundResource(R.drawable.incoming_message_bg);
            viewHolder.messageText.setTextColor(Color.BLACK);
            ((LinearLayout.LayoutParams) viewHolder.messageText.getLayoutParams()).gravity = Gravity.START;
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView messageText;
    }
}
