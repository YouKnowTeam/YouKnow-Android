package com.liumeo.jizhi;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class CollectionActivity extends ListActivity
{
	ArrayList<Message> messages;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ListView listView=getListView();
		messages = new ArrayList<>();
		ListViewAdapter adapter=new MessageItemAdapter(this,messages);
		listView.setAdapter(adapter);
		SQLiteOpenHelper helper=new DBHelper(this);
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.query("collection",new String[]{"msgID","srcID","title","time"},null,null,null,null,null);
		if(cursor.moveToFirst())
		{
			do
			{
				messages.add(new Message(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
			}while(cursor.moveToNext());
		}
		adapter.notifyDataSetChanged();
		cursor.close();
		db.close();
	}
	@Override
	public void onListItemClick(ListView listView, View itemView, int position, long id)
	{

	}
}
