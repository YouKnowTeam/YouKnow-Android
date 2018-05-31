package com.liumeo.jizhi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ArticleActivity extends Activity
{
	TextView contentTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		contentTextView=findViewById(R.id.contentTextView);
		Intent intent=getIntent();
		int msgID=intent.getIntExtra(DiscoveryFragment.MSG_ID,-1);
		//request
	}
}
