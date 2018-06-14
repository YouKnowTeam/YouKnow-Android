package com.liumeo.jizhi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SlideAdapter extends FragmentPagerAdapter
{
	private static int PAGER_COUNT = 2;
	private MeFragment me;
	private DiscoveryFragment discovery;

	public SlideAdapter(FragmentManager fm)
	{
		super(fm);
		discovery = new DiscoveryFragment();
		me = new MeFragment();
	}

	@Override
	public Fragment getItem(int position)
	{
		Fragment fragment = null;
		switch (position)
		{
			case 0:
				fragment = discovery;
				break;
			case 1:
				fragment = me;
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
