package com.momock.fastfood.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.momock.fastfood.ui.R;

public class TabFragment extends Fragment {
	TabHost tHost;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.fragment_tab, container, false);

        tHost = (TabHost) view.findViewById(android.R.id.tabhost);
        tHost.setup();

        TabHost.TabSpec tSpecAlarm = tHost.newTabSpec("alarm");
        tSpecAlarm.setIndicator("Alarm", getResources().getDrawable(R.drawable.ic_action_alarm_2));        
        tSpecAlarm.setContent(new DummyTabContent(this.getActivity()));
        tHost.addTab(tSpecAlarm);
        
        
        TabHost.TabSpec tSpecCalc = tHost.newTabSpec("calculator");
        tSpecCalc.setIndicator("Calc", getResources().getDrawable(R.drawable.ic_action_calculator));        
        tSpecCalc.setContent(new DummyTabContent(this.getActivity()));
        tHost.addTab(tSpecCalc);

        
        TabHost.TabSpec tSpecPlay = tHost.newTabSpec("play");
        tSpecPlay.setIndicator("Play", getResources().getDrawable(R.drawable.ic_action_google_play));        
        tSpecPlay.setContent(new DummyTabContent(this.getActivity()));
        tHost.addTab(tSpecPlay);
        
        TabHost.TabSpec tSpecChart = tHost.newTabSpec("chart");
        tSpecChart.setIndicator("Chart", getResources().getDrawable(R.drawable.ic_action_line_chart));        
        tSpecChart.setContent(new DummyTabContent(this.getActivity()));        
        tHost.addTab(tSpecChart);
        
		return view;
	}
}
