package com.liumeo.jizhi;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity
{
	EditText idEditText;
	EditText passwordEditText;
	View confirmTextView;
	TextView confirmEditText;
	View loginButton;
	View registerButton;
	private static final String userinfoId="userinfoId";
	private static final String userinfoPassword="userinfoPassword";
	private boolean register;//是否为注册模式
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		confirmTextView=findViewById(R.id.confirmTextView);
		confirmEditText=findViewById(R.id.confirmEditText);
		loginButton=findViewById(R.id.loginButton);
		registerButton=findViewById(R.id.registerButton);
		idEditText=findViewById(R.id.idEditText);
		passwordEditText=findViewById(R.id.passwordEditText);
		//默认登录模式
		login(null);
	}
	public void login(View view)
	{
		register=false;
		confirmTextView.setVisibility(View.GONE);
		confirmEditText.setVisibility(View.GONE);
		loginButton.setBackgroundColor(getResources().getColor(R.color.colorOrange));
		registerButton.setBackgroundColor(getResources().getColor(R.color.colorYellow));
		if(getUserInfo())//上次成功登录过
		{
			//submit(null);//自动登录
		}
	}
	public void register(View view)
	{
		register=true;
		confirmTextView.setVisibility(View.VISIBLE);
		confirmEditText.setVisibility(View.VISIBLE);
		loginButton.setBackgroundColor(getResources().getColor(R.color.colorYellow));
		registerButton.setBackgroundColor(getResources().getColor(R.color.colorOrange));
	}
	public void submit(View view)
	{
		final String id=idEditText.getText().toString();
		final String password=passwordEditText.getText().toString();
		Map<String,String>map=new HashMap<>();
		map.put("userid",id);
		map.put("passwd",password);
		if(register)//注册状态
		{
			String confirmPassword=confirmEditText.getText().toString();
			if(!password.equals(confirmPassword))
			{
				Toast toast=Toast.makeText(this,R.string.notConsistent,Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			Networking.post("/SignUp",map,this,new Networking.Updater()
			{
				@Override
				public void run()
				{
					System.out.println(response);
					//判断是否成功
					storeUserInfo(id,password);
					successLogin();
				}
			});
		}
		else//登录状态
		{
			Networking.post("/SignIn",map,this,new Networking.Updater()
			{
				@Override
				public void run()
				{
					System.out.println(response);
					//判断是否成功
					storeUserInfo(id,password);
					successLogin();
				}
			});
		}
	}
	private void successLogin()
	{
		Intent intent=new Intent(this,BottomNavigationActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	private void storeUserInfo(String id,String password)
	{
		SharedPreferences sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(userinfoId,id);
		editor.putString(userinfoPassword,password);
		editor.apply();
	}
	private boolean getUserInfo()
	{
		SharedPreferences sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
		String id=sharedPreferences.getString(userinfoId,"");
		String password=sharedPreferences.getString(userinfoPassword,"");
		idEditText.setText(id);
		passwordEditText.setText(password);
		return !id.equals("");
	}
}
