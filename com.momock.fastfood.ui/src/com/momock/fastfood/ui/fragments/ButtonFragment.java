package com.momock.fastfood.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.momock.fastfood.ui.R;
import com.momock.fastfood.ui.view.MessageListItemView;

public class ButtonFragment extends Fragment{
	MessageListItemView livTest;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.fragment_buttons, container, false);
		
		livTest = (MessageListItemView)view.findViewById(R.id.livTest);
		livTest.setMessageSubject("This is a test");
		livTest.setMessageUnread(true);
		
		return view;
	}

}
