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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity
{
	static final String USERINFO = "USERINFO";
	private static final String USERINFO_ID = "USERINFO_ID";
	private static final String USERINFO_PASSWORD = "USERINFO_PASSWORD";
	static final String LOGOUT ="LOGOUT";
	EditText idEditText;
	EditText passwordEditText;
	View confirmTextView;
	TextView confirmEditText;
	View loginButton;
	View registerButton;
	private boolean register; //是否为注册模式

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		confirmTextView = findViewById(R.id.confirmTextView);
		confirmEditText = findViewById(R.id.confirmEditText);
		loginButton = findViewById(R.id.loginButton);
		registerButton = findViewById(R.id.registerButton);
		idEditText = findViewById(R.id.idEditText);
		passwordEditText = findViewById(R.id.passwordEditText);
		idEditText.setSaveEnabled(false);
		passwordEditText.setSaveEnabled(false);

		//默认登录模式
		showLogin(null);
	}

	/**
	 * 显示登录页
	 *
	 * @param view 触发该动作的view
	 */
	public void showLogin(View view)
	{
		register = false;
		confirmTextView.setVisibility(View.GONE);
		confirmEditText.setVisibility(View.GONE);
		loginButton.setBackgroundColor(getResources().getColor(R.color.colorOrange));
		registerButton.setBackgroundColor(getResources().getColor(R.color.colorYellow));
		if (getUserInfo())//上次成功登录过且不是登出
		{
			submit(null);//自动登录
		}
	}

	/**
	 * 显示注册页
	 *
	 * @param view 触发该动作的view
	 */
	public void showSignup(View view)
	{
		register = true;
		confirmTextView.setVisibility(View.VISIBLE);
		confirmEditText.setVisibility(View.VISIBLE);
		loginButton.setBackgroundColor(getResources().getColor(R.color.colorYellow));
		registerButton.setBackgroundColor(getResources().getColor(R.color.colorOrange));
	}

	/**
	 * 提交表单（注册或登录）
	 *
	 * @param view 触发该动作的view
	 */
	public void submit(View view)
	{
		final String id = idEditText.getText().toString();
		final String password = passwordEditText.getText().toString();
		Map<String, String> params = new HashMap<>();
		params.put("userid", id);
		params.put("passwd", password);
		final Activity thisActivity = this;
		if (register)
		{
			//注册状态
			String confirmPassword = confirmEditText.getText().toString();
			if (!password.equals(confirmPassword))
			{
				Toast toast = Toast.makeText(this, R.string.notConsistent, Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			Networking.post("/SignUp", params, this, new Networking.Updater()
			{
				@Override
				public void run()
				{
					Map result = JSON.parseObject(response, new TypeReference<Map>()
					{
					});
					System.out.println("******************");
					System.out.println(result);
					System.out.println("******************");
					Toast toast;
					int code = (Integer) result.get("code");
					switch (code)
					{
						case 0:
							storeUserInfo(id, password);
							showLogin(null);
							break;
						case -2:
							toast = Toast.makeText(thisActivity, R.string.useridAlreadyExist, Toast.LENGTH_SHORT);
							toast.show();
							break;
						case -3:
							toast = Toast.makeText(thisActivity, R.string.serverError, Toast.LENGTH_SHORT);
							toast.show();
							break;
						default:
							toast = Toast.makeText(thisActivity, R.string.unknownError, Toast.LENGTH_SHORT);
							toast.show();
							break;
					}
				}
			});
		} else
		{
			//登录状态
			Networking.post("/Login", params, this, new Networking.Updater()
			{
				@Override
				public void run()
				{
					Map result = JSON.parseObject(response, new TypeReference<Map>()
					{
					});
					System.out.println("******************");
					System.out.println(result);
					System.out.println("******************");
					Toast toast;
					int code = (Integer) result.get("code");
					switch (code)
					{
						case 0:
							storeUserInfo(id, password);
							Networking.token = (String) result.get("token");
							System.out.println(Networking.token);
							transitToMainView();
							break;
						case -1:
							toast = Toast.makeText(thisActivity, R.string.passwordWrong, Toast.LENGTH_SHORT);
							toast.show();
							break;
						case -2:
							toast = Toast.makeText(thisActivity, R.string.useridNotExist, Toast.LENGTH_SHORT);
							toast.show();
							break;
						case -3:
							toast = Toast.makeText(thisActivity, R.string.serverError, Toast.LENGTH_SHORT);
							toast.show();
							break;
						default:
							toast = Toast.makeText(thisActivity, R.string.unknownError, Toast.LENGTH_SHORT);
							toast.show();
							break;
					}
				}
			});
		}
	}

	/**
	 * 当登录成功后，转到主app界面
	 */
	private void transitToMainView()
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/**
	 * 保存用户名和密码信息到本地，供下次登录使用
	 *
	 * @param id       用户名
	 * @param password 密码
	 */
	private void storeUserInfo(String id, String password)
	{
		SharedPreferences sharedPreferences = getSharedPreferences(USERINFO, MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(USERINFO_ID, id);
		editor.putString(USERINFO_PASSWORD, password);
		editor.apply();
	}

	/**
	 * 在本地寻找保护的用户名和密码信息，供登录使用，自动填充入idEditText和passwordEditText
	 *
	 * @return 如果找到，则返回true，如果没找到，则返回false
	 */
	private boolean getUserInfo()
	{
		SharedPreferences sharedPreferences = getSharedPreferences(USERINFO, MODE_PRIVATE);
		String id = sharedPreferences.getString(USERINFO_ID, "");
		String password = sharedPreferences.getString(USERINFO_PASSWORD, "");
		boolean logout=sharedPreferences.getBoolean(LOGOUT,false);
		idEditText.setText(id);
		passwordEditText.setText(password);
		return !id.equals("")&&!logout;
	}
}
