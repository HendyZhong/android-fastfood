package com.momock.fastfood.activity;

import com.momock.util.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements Constants, OnClickListener{
	Button btnGoTo;
	EditText etNumber1;
	EditText etNumber2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Logger.open("activity.log", Logger.LEVEL_DEBUG);
		Logger.debug("Main: onCreate");
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnGoTo = (Button)findViewById(R.id.btnGoTo);
        etNumber1 = (EditText)findViewById(R.id.etNumber1);
        etNumber2 = (EditText)findViewById(R.id.etNumber2);
        
        btnGoTo.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		Intent intentToSecond = new Intent(this, SecondActivity.class);
		intentToSecond.putExtra(KEY_NUM1, Double.parseDouble("0" + etNumber1.getText()));
		intentToSecond.putExtra(KEY_NUM2, Double.parseDouble("0" + etNumber2.getText()));
		this.startActivityForResult(intentToSecond, REQUEST_CODE_CALC);	
		Logger.debug(KEY_NUM1 + " = " + etNumber1.getText());
		Logger.debug(KEY_NUM2 + " = " + etNumber2.getText());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logger.debug("requestCode " + requestCode + " resultCode = " + resultCode);
		if (requestCode == REQUEST_CODE_CALC)
		{
			Toast.makeText(this, "The result is " + data.getExtras().getDouble(KEY_RESULT), Toast.LENGTH_LONG).show();
		}
		else
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Logger.debug("Main: onSaveInstanceState");		
		//super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Logger.debug("Main: onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}	

	@Override
	protected void onDestroy() {
		Logger.debug("Main: onDestroy");
		super.onDestroy();
		Logger.close();
	}

	@Override
	protected void onRestart() {
		Logger.debug("Main: onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Logger.debug("Main: onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Logger.debug("Main: onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Logger.debug("Main: onStop");
		super.onStop();
	}

	@Override
	protected void onPause() {
		Logger.debug("Main: onPause");
		super.onPause();
	}
}
