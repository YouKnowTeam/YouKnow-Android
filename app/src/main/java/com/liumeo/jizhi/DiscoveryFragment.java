package com.liumeo.jizhi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
	static final String MSG_ID="MSG_ID";
    /**
     * 储存所有message的ArrayList
     */
    ArrayList<Message> messages;
    /**
     * 适配messages的adapter
     */
    ListViewAdapter adapter;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    this.messages = new ArrayList<>();
	    this.adapter = new MessageItemAdapter(getContext(), messages);
		setListAdapter(this.adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

    /**
     * 从服务器请求数据
     * 自动更新至页面
     * @param fromMsgID 之前拿到的最后一条message的msgID
     * @param num 需要取多少条message
     */
	private void getData(int fromMsgID, int num) {
        Map<String,String> params=new HashMap<>();
        params.put("token", Networking.token);
        params.put("msg_id", String.valueOf(fromMsgID));
        params.put("num", String.valueOf(num));
        final Activity thisActivity = this.getActivity();
        final DiscoveryFragment thisFragment = this;
        Networking.get("/GetAllSubscribedMessages", params, this.getActivity(), new Networking.Updater() {
            @Override
            public void run() {
                Map result = JSON.parseObject(response, new TypeReference<Map>(){});
                System.out.println("******************");
                System.out.println(result);
                System.out.println("******************");
                Toast toast;
                int code = (Integer)result.get("code");
                switch (code) {
                    case 0:
                        JSONArray data = (JSONArray)result.get("data");
                        for (Object oneData: data) {
                            JSONObject oneDataObject = (JSONObject)oneData;
                            int msgID = (int)(oneDataObject.get("MsgID"));
                            String srcID = (String)(oneDataObject.get("SrcID"));
                            String brief = (String)(oneDataObject.get("Brief"));
                            String timestamp = (String)(oneDataObject.get("Timestamp"));
                            thisFragment.messages.add(new Message(msgID, srcID, brief, timestamp));
                        }
                        thisFragment.adapter.notifyDataSetChanged();
                        break;
                    case -1:
                        toast=Toast.makeText(thisActivity,R.string.tokenInvalid,Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case -3:
                        toast=Toast.makeText(thisActivity,R.string.serverError,Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    default:
                        toast=Toast.makeText(thisActivity,R.string.unknownError,Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        });
    }

	@Override
	public void onStart()
	{
		super.onStart();
		getData(-1, 10);
		ListView listView=getListView();
		listView.setDividerHeight(0);//取消边框线
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Intent intent=new Intent(getActivity(),ArticleActivity.class);
				intent.putExtra(MSG_ID,messages.get(i).msgID);
				startActivity(intent);
			}
		});
	}
}