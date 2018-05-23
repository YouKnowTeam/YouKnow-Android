package com.liumeo.jizhi;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends ListFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ArrayList<Article>articles=new ArrayList<Article>();
		articles.add(new Article("四六级报名开始","2018-03-11"));
		articles.add(new Article("16级xxx有没有男/女朋友呀","2018-05-05"));
		ListAdapter adapter=new ListViewAdapter<Article> (getContext(),articles,R.layout.article_item)
		{
			@Override
			public void convert(ViewHolder holder, Article o)
			{
				((TextView)holder.getView(R.id.title)).setText(o.title);
				((TextView)holder.getView(R.id.time)).setText(o.time);
			}
		};
		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		getListView().setDividerHeight(0);//取消边框线
	}
}