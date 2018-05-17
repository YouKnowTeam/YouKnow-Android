package com.liumeo.jizhi;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class BottomNavigationActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener
{
	private ViewPager viewPager;
	private Menu menu;
	private MenuItem menuItem;
	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener()
	{

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item)
		{
			switch (item.getItemId())
			{
				case R.id.navigation_home:
					viewPager.setCurrentItem(0);
					return true;
				case R.id.navigation_dashboard:
					viewPager.setCurrentItem(1);
					return true;
			}
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bottom_navigation);

		viewPager=(ViewPager)findViewById(R.id.viewPager);
		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
		menu=navigation.getMenu();
		menuItem=menu.getItem(0);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}

			@Override
			public void onPageSelected(int position)
			{
				menuItem.setChecked(false);
				menuItem=menu.getItem(position);
				menuItem.setChecked(true);
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});
		viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
	}

	@Override
	public void onFragmentInteraction(Uri uri)
	{

	}
}
