package com.momock.fastfood.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.momock.fastfood.ui.fragments.ButtonFragment;
import com.momock.fastfood.ui.fragments.MessageFragment;
import com.momock.fastfood.ui.fragments.TabFragment;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Fragment fragment;
        fragment = new ButtonFragment();
        //fragment = new ListViewFragment(); 
        //fragment = new MessageFragment();
        //fragment = new TabFragment();
        FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.fragment_content, fragment);
		ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
