package com.momock.fastfood.ui.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.momock.fastfood.ui.model.Message;

public class MessageListAdapter extends BaseAdapter {
	Message[] messages;
	public MessageListAdapter(Message[] msgs)
	{
		messages = msgs;
	}
    @Override
    public int getCount() {
        return messages.length;
    }

    @Override
    public Object getItem(int position) {
        return messages[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MessageListItemView messageListItemView = (MessageListItemView) convertView;

        if (messageListItemView == null) {
            messageListItemView = new MessageListItemView(viewGroup.getContext());
        }

        Message message = (Message) getItem(position);
        messageListItemView.setMessageSubject(message.getSubject());
        messageListItemView.setMessageUnread(message.isUnread());

        return messageListItemView;
    }
}
