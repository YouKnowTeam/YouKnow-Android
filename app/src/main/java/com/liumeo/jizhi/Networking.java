package com.liumeo.jizhi;

import android.app.Activity;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class Networking
{
	private static final String prefix = "127.0.0.1";
	static void get(String path, Map<String,String> arguments, final Activity activity, final Updater updater)
	{
		if(arguments!=null)
		{
			StringBuilder sb=new StringBuilder(path).append("?");
			boolean first=true;
			for (Object o : arguments.entrySet())
			{
				if (!first)
				{
					sb.append("&");
				}
				Map.Entry entry = (Map.Entry) o;
				sb.append(entry.getKey()).append('=').append(entry.getValue());
				first = false;
			}
			path=sb.toString();
		}
		Request request = new Request.Builder()
				.url(prefix + path)
				.build();
		send(request,activity,updater);
	}
	static void post(String path, Map<String,String> arguments, final Activity activity, final Updater updater)
	{
		FormBody.Builder builder = new FormBody.Builder();
		if(arguments!=null)
		{
			for (Object o : arguments.entrySet())
			{
				Map.Entry entry = (Map.Entry) o;
				builder.add(entry.getKey().toString(),entry.getValue().toString());
			}
		}
		RequestBody body=builder.build();
		Request request = new Request.Builder()
				.url(prefix + path)
				.post(body)
				.build();
		send(request,activity,updater);
	}
	private static void send(Request request, final Activity activity, final Updater updater)
	{
		OkHttpClient okHttpClient = new OkHttpClient();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, IOException e)
			{
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response)
			{
				activity.runOnUiThread(updater);
			}
		});
	}

	static class Updater implements Runnable
	{
		String response;
		Updater(String response)
		{
			this.response=response;
		}
		public void run()
		{
		}
	}
}
