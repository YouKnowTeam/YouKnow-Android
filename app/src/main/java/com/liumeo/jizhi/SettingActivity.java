package com.liumeo.jizhi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends Activity
{
    ArrayList<CheckBoxItem> checkBoxes;
    LinearLayout sourceRelativeLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		this.sourceRelativeLayout = findViewById(R.id.sourceLinearLayout);
		getSources();
	}
	private void getSources()
	{
        Map<String,String> params=new HashMap<>();
        params.put("token", Networking.token);
        final SettingActivity thisActivity = this;
        Networking.get("/GetAllMessageSource", params, this, new Networking.Updater() {
            @Override
            public void run() {
                Map result = JSON.parseObject(response, new TypeReference<Map>(){});
                System.out.println("******************");
                System.out.println(result);
                System.out.println("******************");
                Toast toast;
                int code = (Integer)result.get("code");
                switch (code) {
                    case 0:
                        thisActivity.checkBoxes=new ArrayList<>();
                        JSONArray data = (JSONArray)result.get("data");
                        for (Object oneData: data) {
                            JSONObject oneDataObject = (JSONObject)oneData;
                            String srcID = (String)(oneDataObject.get("SrcID"));
                            String srcDesc = (String)(oneDataObject.get("SrcDesc"));
                            int flag = (int)(oneDataObject.get("flag"));
                            boolean checked = (flag == 1);

                            View.OnClickListener listener=new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    String srcID=(String)view.getTag();
                                    final boolean checked = ((CheckBox)view).isChecked();
                                    String path;
                                    if (checked)
                                        path = "/SubscribeMessageSource";
                                    else
                                        path = "/UnsubscribeMessageSource";
                                    Map<String, String> params = new HashMap<>();
                                    params.put("token", Networking.token);
                                    params.put("source_id", srcID);
                                    final View thisView = view;
                                    Networking.post(path, params, thisActivity, new Networking.Updater()
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
                                                    ((CheckBox)thisView).setChecked(checked);
                                                    break;
                                                case -1:
                                                    toast = Toast.makeText(thisActivity, R.string.tokenInvalid, Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    ((CheckBox)thisView).setChecked(!checked);
                                                    break;
                                                case -3:
                                                    toast = Toast.makeText(thisActivity, R.string.serverError, Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    ((CheckBox)thisView).setChecked(!checked);
                                                    break;
                                                default:
                                                    toast = Toast.makeText(thisActivity, R.string.unknownError, Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    ((CheckBox)thisView).setChecked(!checked);
                                                    break;
                                            }
                                        }
                                    });

                                }
                            };

                            CheckBoxItem item = new CheckBoxItem(srcDesc, srcID);
                            item.checkBox = new CheckBox(thisActivity);
                            item.checkBox.setText(item.text);
                            item.checkBox.setTag(item.srcID);
                            item.checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            item.checkBox.setOnClickListener(listener);
                            item.checkBox.setChecked(checked);
                            thisActivity.sourceRelativeLayout.addView(item.checkBox);
                            thisActivity.checkBoxes.add(item);
                        }
                        break;
                    case -1:
                        toast=Toast.makeText(thisActivity,R.string.tokenInvalid,Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case -3:
                        toast=Toast.makeText(thisActivity,R.string.serverError,Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    default:
                        toast=Toast.makeText(thisActivity,R.string.unknownError,Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        });
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
