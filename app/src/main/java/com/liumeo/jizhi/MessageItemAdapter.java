package com.liumeo.jizhi;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

public class MessageItemAdapter extends ListViewAdapter<Message>
{
	MessageItemAdapter(Context context, List<Message> datas)
	{
		super(context,datas,R.layout.messsage_item);
	}
	@Override
	public void convert(ViewHolder holder, Message message)
	{
		((TextView)holder.getView(R.id.sourceTextView)).setText(message.srcID);
		((TextView)holder.getView(R.id.titleTextView)).setText(message.brief);
		((TextView)holder.getView(R.id.timeTextView)).setText(message.time);
	}
}
