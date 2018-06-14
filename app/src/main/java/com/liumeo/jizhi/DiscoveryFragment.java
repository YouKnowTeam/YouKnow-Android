package com.liumeo.jizhi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiscoveryFragment extends ListFragment
{
	/**
	 * 储存所有message的ArrayList
	 */
	ArrayList<Message> messages;
	/**
	 * 适配messages的adapter
	 */
	ListViewAdapter adapter;
	int lastVisibleItem;
	boolean hasMore = true;

	void setLastVisibleItem(int lastVisibleItem)
	{
		this.lastVisibleItem = lastVisibleItem;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 从服务器请求数据
	 * 自动更新至页面
	 *
	 * @param fromMsgID 之前拿到的最后一条message的msgID
	 * @param num       需要取多少条message
	 */
	private void getData(int fromMsgID, int num)
	{
		Map<String, String> params = new HashMap<>();
		params.put("token", Networking.token);
		params.put("msg_id", String.valueOf(fromMsgID));
		params.put("num", String.valueOf(num));
		final Activity thisActivity = this.getActivity();
		final DiscoveryFragment thisFragment = this;
		Networking.get("/GetAllSubscribedMessages", params, this.getActivity(), new Networking.Updater()
		{
			@Override
			public void run()
			{
				Map result = JSON.parseObject(response, new TypeReference<Map>()
				{
				});
				System.out.println("******************");
				System.out.println(result);
				System.out.println("******************");
				Toast toast;
				int code = (Integer) result.get("code");
				switch (code)
				{
					case 0:
						JSONArray data = (JSONArray) result.get("data");
						if (data.size() == 0)
						{
							noMore();
							return;
						}
						for (Object oneData : data)
						{
							JSONObject oneDataObject = (JSONObject) oneData;
							int msgID = (int) (oneDataObject.get("MsgID"));
							String srcID = (String) (oneDataObject.get("SrcID"));
							String brief = (String) (oneDataObject.get("Brief"));
							String time = (String) (oneDataObject.get("Timestamp"));
							time = time.replace('T', ' ').replace('Z', ' ');
							time = time.substring(0, time.indexOf('.'));
							thisFragment.messages.add(new Message(msgID, srcID, brief, time));
						}
						thisFragment.adapter.notifyDataSetChanged();
						break;
					case -1:
						toast = Toast.makeText(thisActivity, R.string.tokenInvalid, Toast.LENGTH_SHORT);
						toast.show();
						break;
					case -3:
						toast = Toast.makeText(thisActivity, R.string.serverError, Toast.LENGTH_SHORT);
						toast.show();
						break;
					default:
						toast = Toast.makeText(thisActivity, R.string.unknownError, Toast.LENGTH_SHORT);
						toast.show();
						break;
				}
			}
		});
	}

	void noMore()
	{
		hasMore = false;
		View view = getListView();
		ProgressBar loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
		TextView loadingTextView = view.findViewById(R.id.loadMoreView);
		loadingProgressBar.setVisibility(View.GONE);
		loadingTextView.setText(R.string.noMore);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (!Global.needRefresh)
		{
			return;
		}
		messages.clear();
		getData(-1, 10);
		Global.needRefresh = false;
	}

	@Override
	public void onActivityCreated(Bundle bundle)
	{
		super.onActivityCreated(bundle);
		Global.needRefresh = true;
		this.messages = new ArrayList<>();
		this.adapter = new MessageItemAdapter(getContext(), messages);
		ListView listView = getListView();
		View loadMoreView = View.inflate(getContext(), R.layout.loading_view, null);
		listView.addFooterView(loadMoreView);
		listView.setDividerHeight(0);//取消边框线
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				if (i >= messages.size())
				{
					return;
				}
				Intent intent = new Intent(getActivity(), ArticleActivity.class);
				intent.putExtra(ArticleActivity.MSG_ID, messages.get(i).msgID);
				intent.putExtra(ArticleActivity.TITLE, messages.get(i).title);
				startActivity(intent);
			}
		});
		listView.setOnScrollListener(new AbsListView.OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView absListView, int scrollState)
			{
				if (scrollState == SCROLL_STATE_IDLE && lastVisibleItem > adapter.getCount() && hasMore)
				{
					getData(messages.get(messages.size() - 1).msgID, 10);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				setLastVisibleItem(firstVisibleItem + visibleItemCount);
			}
		});
		setListAdapter(this.adapter);
	}
}