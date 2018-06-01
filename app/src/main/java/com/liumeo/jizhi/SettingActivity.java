package com.liumeo.jizhi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class SettingActivity extends Activity
{
	CheckBoxItem[]checkBoxes;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		LinearLayout sourceRelativeLayout=findViewById(R.id.sourceLinearLayout);
		checkBoxes=new CheckBoxItem[]{new CheckBoxItem(getString(R.string.tree_hole),""),new CheckBoxItem(getString(R.string.dean),"")};
		for(CheckBoxItem checkBoxItem:checkBoxes)
		{
			checkBoxItem.checkBox=new CheckBox(this);
			checkBoxItem.checkBox.setText(checkBoxItem.text);
			checkBoxItem.checkBox.setTag(checkBoxItem.srcID);
			checkBoxItem.checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			sourceRelativeLayout.addView(checkBoxItem.checkBox);
		}
		getSources();
	}
	private void getSources()
	{
		//request
		new Networking.Updater()
		{
			@Override
			public void run()
			{
				for(CheckBoxItem checkBoxItem:checkBoxes)
				{
					if(checkBoxItem.srcID=="")
					{
						checkBoxItem.checkBox.setChecked(true);
					}
				}
			}
		};
	}
	public void checkBoxClicked(View view)
	{
		String srcID=(String)view.getTag();
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
