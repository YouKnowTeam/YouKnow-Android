package com.liumeo.jizhi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{
	View confirmTextView;
	View confirmEditText;
	View loginButton;
	View registerButton;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		confirmTextView=findViewById(R.id.confirmTextView);
		confirmEditText=findViewById(R.id.confirmEditText);
		loginButton=findViewById(R.id.loginButton);
		registerButton=findViewById(R.id.registerButton);
		login(null);
	}
	public void login(View view)
	{
		getResources();
		confirmTextView.setVisibility(View.GONE);
		confirmEditText.setVisibility(View.GONE);
		loginButton.setBackgroundColor(getResources().getColor(R.color.colorOrange));
		registerButton.setBackgroundColor(getResources().getColor(R.color.colorYellow));
	}
	public void register(View view)
	{
		confirmTextView.setVisibility(View.VISIBLE);
		confirmEditText.setVisibility(View.VISIBLE);
		loginButton.setBackgroundColor(getResources().getColor(R.color.colorYellow));
		registerButton.setBackgroundColor(getResources().getColor(R.color.colorOrange));
	}
	public void submit(View view)
	{
		Intent intent=new Intent(this,BottomNavigationActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
