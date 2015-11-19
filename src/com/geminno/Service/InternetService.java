package com.geminno.Service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.utils.URIUtils;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.webkit.URLUtil;

import com.geminno.Bean.Constants;
import com.geminno.Utils.InternetUtils;
import com.geminno.Utils.ServletUtil;

/**
 * 
 * @ClassName: InternetService
 * @Description: 判断网络状态
 * @author: XU
 * @date: 2015年10月30日 下午4:27:41
 */
public class InternetService extends IntentService {
    private static final String NAME = "InternetService";
    private static final String ACTION_CHECK_NET = "com.geminno.service.CHECK_NET";
    private InternetUtils internetUtils;
    private Intent intent;
    private LocalBroadcastManager localBroadcastManager;
    public static String ServletURL;
    private SharedPreferences sharedPreferences;
    private Editor editor;
    public static String IP;

    public InternetService() {
	super(NAME);
	internetUtils = InternetUtils.getInstence();
	localBroadcastManager = LocalBroadcastManager.getInstance(this);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
	editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	if (ACTION_CHECK_NET.equals(intent.getAction())) {
	    if (internetUtils.isNetworkAvailable(this)) {
		// 存储网络状态
		editor.putBoolean(Constants.NETWORK_STATUS, true).apply();
		// 检测服务器状态
		if (checkServlet()) {
		    // 存储服务器状态
		    editor.putBoolean(Constants.SERVER_STATUS, true).apply();
		    // 发布广播开始加载数据
		    intent = new Intent();
		    intent.setAction("com.geminno.reciver.NET_CONNECTED");
		    localBroadcastManager.sendBroadcast(intent);
		} else {
		    editor.putBoolean(Constants.SERVER_STATUS, false).apply();
		    intent = new Intent();
		    intent.setAction("com.geminno.reciver.TIPS");
		    intent.putExtra("TIPS", "服务器连接失败");
		    localBroadcastManager.sendBroadcast(intent);
		}
	    } else {
		// 存储网络状态
		editor.putBoolean(Constants.NETWORK_STATUS, false).apply();
		intent = new Intent();
		intent.setAction("com.geminno.reciver.TIPS");
		intent.putExtra("TIPS", "网络不可用");
		localBroadcastManager.sendBroadcast(intent);
	    }
	}
    }

    private boolean checkServlet() {
	ApplicationInfo applicationInfo = null;

	try {
	    applicationInfo = getPackageManager().getApplicationInfo(
		    getPackageName(), PackageManager.GET_META_DATA);

	    IP = applicationInfo.metaData.getString("IP");
	    ServletURL = "http://" + IP + ":8080/HiWeek/servlet/client/";

	    return ServletUtil.getInstence().PingURL(
		    new URL(ServletURL + "CheckIP"));
	} catch (MalformedURLException | NameNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return false;
	}
    }
}
