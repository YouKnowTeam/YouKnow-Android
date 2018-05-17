package com.liumeo.jizhi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter
{
	private static int PAGER_COUNT=2;
	private BlankFragment me;
	private ItemFragment discovery;
	public MyFragmentPagerAdapter(FragmentManager fm)
	{
		super(fm);
		discovery=new ItemFragment();
		me=new BlankFragment();
	}

	@Override
	public Fragment getItem(int position)
	{
		Fragment fragment=null;
		switch (position)
		{
			case 0:
				fragment=discovery;
				break;
			case 1:
				fragment=me;
				break;
		}
		return fragment;
	}

	@Override
	public int getCount()
	{
		return PAGER_COUNT;
	}
}
