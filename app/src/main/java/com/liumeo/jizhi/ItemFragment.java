package com.liumeo.jizhi;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ItemFragment extends ListFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ArrayAdapter<Object>adapter=new ArrayAdapter<Object>(inflater.getContext(),android.R.layout.simple_list_item_1,new Object[]{"2","3"});
		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}