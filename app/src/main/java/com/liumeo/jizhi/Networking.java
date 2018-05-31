package com.liumeo.jizhi;

import android.app.Activity;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求类
 */
class Networking
{
	/**
	 * server prefix
	 */
	private static final String prefix = "http://162.105.175.115:4000";
	/**
	 * 用作登录凭证，除SignUp和Login外的所有请求都必须带上
	 */
	public static String token = "";

	/**
	 * 发起GET请求
	 * @param path 请求路径
	 * @param arguments 参数（被url-encode入URL）
	 * @param activity 用于返回主线程的activity
	 * @param updater 请求返回后的回调函数
	 */
	static void get(String path, Map<String,String> arguments, final Activity activity, final Updater updater)
	{
		if(arguments != null)
		{
			StringBuilder sb = new StringBuilder(path).append("?");
			boolean first=true;
			for (Object o: arguments.entrySet())
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

	/**
	 * 发起POST请求
	 * @param path 请求路径
	 * @param arguments 参数（被url-encode入body）
	 * @param activity 用于返回主线程的activity
	 * @param updater 请求返回后的回调函数
	 */
	static void post(String path, Map<String,String> arguments, final Activity activity, final Updater updater)
	{
		FormBody.Builder builder = new FormBody.Builder();
		if(arguments!=null)
		{
			for (Object o: arguments.entrySet())
			{
				Map.Entry entry = (Map.Entry) o;
				builder.add(entry.getKey().toString(),entry.getValue().toString());
			}
		}
		RequestBody body = builder.build();
		Request request = new Request.Builder()
				.url(prefix + path)
				.post(body)
				.build();
		send(request,activity,updater);
	}

	/**
	 * 内部方法，用于发出请求，供get和post函数调用
	 * @param request get和post给出的request请求体
	 * @param activity 用于返回主线程的activity
	 * @param updater 请求返回后的回调函数
	 */
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
				activity.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						Toast toast = Toast.makeText(activity, R.string.networkError, Toast.LENGTH_SHORT);
						toast.show();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response)
			{
				try
				{
					updater.response=response.body().string();
					System.out.println("******************");
					System.out.println(updater.response);
					System.out.println("******************");
					activity.runOnUiThread(updater);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 请求返回后的回调函数类
	 * 使用时请为response赋值网络请求返回值
	 * 并实现run方法
	 */
	static class Updater implements Runnable
	{
		String response;
		@Override
		public void run()
		{
		}
	}
}
