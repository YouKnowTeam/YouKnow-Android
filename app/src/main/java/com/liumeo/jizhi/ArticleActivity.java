package com.liumeo.jizhi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.Map;

public class ArticleActivity extends Activity
{
	TextView titleTextView;
	TextView contentTextView;
	Switch collectSwitch;
	int msgID;
	String srcID;
	String title;
	String time;
	String content;

	void setTitle(String title)
	{
		this.title = title;
	}

	void setTime(String time)
	{
		this.time = time;
	}

	void setContent(String content)
	{
		this.content = content;
	}
	void setMsgID(int msgID)
	{
		this.msgID=msgID;
	}
	void setSrcID(String srcID)
	{
		this.srcID=srcID;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		titleTextView=findViewById(R.id.titleTextView);
		contentTextView=findViewById(R.id.contentTextView);
		collectSwitch=findViewById(R.id.collectSwitch);
		Intent intent=getIntent();
		int msgID=intent.getIntExtra(DiscoveryFragment.MSG_ID,-1);
		Map<String,String> params=new HashMap<>();
		params.put("token", Networking.token);
		params.put("msg_id", String.valueOf(msgID));
		Networking.get("/GetMessageDetail",params,this,new Networking.Updater(){
			@Override
			public void run()
			{
				Map result = JSON.parseObject(response, new TypeReference<Map>(){});
				System.out.println("******************");
				System.out.println(result);
				System.out.println("******************");
				Toast toast;
				int code = (Integer)result.get("code");
				switch (code) {
					case 0:
						JSONArray data = (JSONArray)result.get("data");
						for (Object oneData: data) {
							JSONObject oneDataObject = (JSONObject)oneData;
							setMsgID((int)(oneDataObject.get("MsgID")));
							setSrcID((String)(oneDataObject.get("SrcID")));
							setContent((String)(oneDataObject.get("Detail")));
							setTime((String)(oneDataObject.get("Timestamp")));
						}
						updateView();
						break;
					case -1:
						toast=Toast.makeText(ArticleActivity.this,R.string.tokenInvalid,Toast.LENGTH_SHORT);
						toast.show();
						break;
					case -3:
						toast=Toast.makeText(ArticleActivity.this,R.string.serverError,Toast.LENGTH_SHORT);
						toast.show();
						break;
					default:
						toast=Toast.makeText(ArticleActivity.this,R.string.unknownError,Toast.LENGTH_SHORT);
						toast.show();
						break;
				}
			}
		});
	}
	void updateView()
	{
		titleTextView.setText(title+" "+srcID+" "+time);
		contentTextView.setText(content);
		SQLiteOpenHelper helper=new DBHelper(this);
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.query("collection",new String[]{"count(*)"},"msgID=?",new String[]{Integer.toString(msgID)},null,null,null);
		cursor.moveToFirst();
		if(cursor.getInt(0)>0)
		{
			collectSwitch.setChecked(true);
		}
		cursor.close();
		db.close();
	}
	public void onSwitchClicked(View view)
	{
		boolean on=((Switch)view).isChecked();
		SQLiteOpenHelper helper=new DBHelper(this);
		SQLiteDatabase db=helper.getWritableDatabase();
		if(on)
		{
			db.execSQL("insert into collection (msgID, srcID, title, time, content) values (?,?,?,?,?)",new String[]{Integer.toString(msgID),srcID,title,time,content});
		}
		else
		{
			db.execSQL("delete from collection where msgId=?",new String[]{Integer.toString(msgID)});
		}
		db.close();
	}
}
