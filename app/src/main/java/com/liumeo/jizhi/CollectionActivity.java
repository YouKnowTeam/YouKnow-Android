package com.liumeo.jizhi;

import android.app.ListActivity;
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
	}
	@Override
	public void onListItemClick(ListView listView, View itemView, int position, long id)
	{

	}
}
