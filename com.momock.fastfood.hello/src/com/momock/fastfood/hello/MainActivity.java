package com.momock.fastfood.hello;

import com.momock.util.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button btnSay;
	EditText etName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Logger.open("hello.log", Logger.LEVEL_DEBUG);
        
        setContentView(R.layout.activity_main);
        btnSay = (Button)this.findViewById(R.id.btnSay);
        etName = (EditText)this.findViewById(R.id.etName);
        btnSay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Logger.debug("Say Hello " + etName.getText());
				Toast.makeText(MainActivity.this, "Hello " + etName.getText(), Toast.LENGTH_SHORT).show();
				
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.close();
	}
}
