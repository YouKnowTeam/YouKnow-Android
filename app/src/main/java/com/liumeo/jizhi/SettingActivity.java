package com.liumeo.jizhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}
	public void checkBoxClicked(View view)
	{
		//消息源更新请求
	}
	public void logout(View view)
	{
		Networking.token="";
		SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USERINFO, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(LoginActivity.LOGOUT,true);
		editor.apply();
		Intent intent=new Intent(this,LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
