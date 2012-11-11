package com.momock.fastfood.db;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.momock.fastfood.db.model.Person;

public class MainActivity extends Activity {
	ListView lvPersons;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Person> persons = App.getInstance().getPersonDao().getAllPersons();
        lvPersons = (ListView)this.findViewById(R.id.lvPersons);
        lvPersons.setAdapter(new ArrayAdapter<Person>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                persons));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
