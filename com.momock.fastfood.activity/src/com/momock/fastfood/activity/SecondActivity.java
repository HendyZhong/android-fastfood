package com.momock.fastfood.activity;

import com.momock.util.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends Activity implements Constants, OnClickListener{

	Button btnBack;
	Button btnCount;
	int count = 0;
	double result = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Logger.debug("Second: onCreate");
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        
        btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnCount = (Button)findViewById(R.id.btnCount);
        btnCount.setOnClickListener(this);
        
        if (savedInstanceState != null)      
        {
        	count = savedInstanceState.getInt(KEY_COUNT);
        	Logger.debug("get count = " + count + " in onCreate");
        }

        double num1 = this.getIntent().getExtras().getDouble(KEY_NUM1);
        double num2 = this.getIntent().getExtras().getDouble(KEY_NUM2);
        result = num1 + num2;

		Logger.debug(KEY_NUM1 + " = " + num1);
		Logger.debug(KEY_NUM2 + " = " + num2);
		Logger.debug("Result = " + result);
		Logger.debug("Count = " + count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_second, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btnBack:
			Intent intentResult = new Intent();
			intentResult.putExtra(KEY_RESULT, result);
			this.setResult(RESULT_OK, intentResult);
			finish();
			break;
		case R.id.btnCount:
			count ++;
			Toast.makeText(this, "Count = " + count, Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Logger.debug("Second: onSaveInstanceState");
		
		//outState.putInt(KEY_COUNT, count);	
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Logger.debug("Second: onRestoreInstanceState");

        if (savedInstanceState != null)      
        {
        	count = savedInstanceState.getInt(KEY_COUNT);
        	Logger.debug("get count = " + count + " in onRestoreInstanceState");
        }

		super.onRestoreInstanceState(savedInstanceState);
	}	
	
	@Override
	protected void onDestroy() {
		Logger.debug("Second: onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		Logger.debug("Second: onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Logger.debug("Second: onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Logger.debug("Second: onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Logger.debug("Second: onStop");
		super.onStop();
	}

	@Override
	protected void onPause() {
		Logger.debug("Second: onPause");
		super.onPause();
	}
}
