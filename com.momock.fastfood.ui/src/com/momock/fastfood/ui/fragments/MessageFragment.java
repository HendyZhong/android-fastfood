package com.momock.fastfood.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.momock.fastfood.ui.App;
import com.momock.fastfood.ui.view.MessageListAdapter;
import com.momock.fastfood.ui.view.MessageListItemView;

public class MessageFragment extends ListFragment{

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.setListAdapter(new MessageListAdapter(App.getMessages()));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		MessageListItemView liv = (MessageListItemView)v;
		liv.toggleMessageUnread();
	}

}
