package com.liumeo.jizhi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemFragment extends ListFragment
{

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
	    this.adapter = new ListViewAdapter<Message> (getContext(), messages,R.layout.article_item)
        {
            @Override
            public void convert(ViewHolder holder, Message o)
            {
                ((TextView)holder.getView(R.id.title)).setText(o.brief);
                ((TextView)holder.getView(R.id.time)).setText(o.time);
            }
        };

	    getData(-1, 10);
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
        final ItemFragment thisFragment = this;
        Networking.get("/GetAllSubscribedMessages", params, this.getActivity(), new Networking.Updater() {
            @Override
            public void run() {
                System.out.println("******************");
                System.out.println(response);
                System.out.println("******************");
                Map result = JSON.parseObject(response, new TypeReference<Map>(){});
                System.out.println("******************");
                System.out.println(result);
                System.out.println("******************");Toast toast;
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
		getListView().setDividerHeight(0);//取消边框线
	}
}