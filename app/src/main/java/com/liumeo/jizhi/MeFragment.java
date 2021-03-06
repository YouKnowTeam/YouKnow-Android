package com.liumeo.jizhi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MeFragment extends Fragment
{

	private OnFragmentInteractionListener mListener;
	private ListView meListView;
	private TextView nowUserTextView;

	public MeFragment()
	{
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_me, container, false);
	}

	@Override
	public void onActivityCreated(Bundle bundle)
	{
		super.onActivityCreated(bundle);
		View view = getView();
		meListView = view.findViewById(R.id.meListView);
		nowUserTextView = view.findViewById(R.id.nowUserTextView);
		nowUserTextView.setText(getString(R.string.nowUser) + Global.id);
		//ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,new String[]{getResources().getString(R.string.setting),getResources().getString(R.string.collect)});
		//meListView.setAdapter(adapter);
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("iconImageView", R.drawable.setting);
		map.put("nameTextView", getResources().getString(R.string.setting));
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("iconImageView", R.drawable.collect);
		map.put("nameTextView", getResources().getString(R.string.collect));
		list.add(map);
		SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.simple_item, new String[]{"iconImageView", "nameTextView"}, new int[]{R.id.iconImageView, R.id.nameTextView});
		meListView.setAdapter(adapter);
		meListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Intent intent;
				switch (i)
				{
					case 0:
						intent = new Intent(getActivity(), SettingActivity.class);
						startActivity(intent);
						break;
					case 1:
						intent = new Intent(getActivity(), CollectionActivity.class);
						startActivity(intent);
					default:
						break;
				}
			}
		});
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri)
	{
		if (mListener != null)
		{
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener)
		{
			mListener = (OnFragmentInteractionListener) context;
		} else
		{
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener
	{
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
