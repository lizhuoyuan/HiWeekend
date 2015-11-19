package com.geminno.Fragment.home;

import com.geminno.Activities.setting.MyOrderActivity;
import com.geminno.Activities.setting.MyselfActivity;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.Constants;
import com.geminno.Service.InternetService;
import com.geminno.Utils.ImageUtils;
import com.geminno.Utils.MyPropertiesUtil;
import com.geminno.hiweek1_0.R;
import com.google.android.support.v4.view.ViewPager.LayoutParams;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author 李卓原 创建时间：2015年10月20日 下午3:02:21
 */
public class PersonCenterFragment extends Fragment implements OnClickListener {
    View root;
    TextView tvcredit, tvmoney, tel, mima, jine;
    ImageView img;
    Button login_bt, moneychongzhi, guanbi;
    LinearLayout chongzhi;
    String newtel, newmima;
    double newjine;
    PopupWindow pouup;
    public static boolean login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	login = PreferenceManager.getDefaultSharedPreferences(getContext())
		.getBoolean(Constants.USER_STATUS, false);
	root = inflater.inflate(R.layout.fragment_c, null);

	init();
	return root;
    }

    public void init() {
	tvcredit = (TextView) root.findViewById(R.id.textcredit);
	tvmoney = (TextView) root.findViewById(R.id.textmymoney);
	chongzhi = (LinearLayout) root.findViewById(R.id.chong_zhi);
	LinearLayout yijian = (LinearLayout) root
		.findViewById(R.id.yijianfankui);
	yijian.setOnClickListener(this);
	LinearLayout guanyu = (LinearLayout) root.findViewById(R.id.guanyu);
	guanyu.setOnClickListener(this);
	chongzhi.setOnClickListener(this);
	LinearLayout collect = (LinearLayout) root.findViewById(R.id.collect);
	collect.setOnClickListener(this);
	LinearLayout order = (LinearLayout) root.findViewById(R.id.myorder);
	order.setOnClickListener(this);
	LinearLayout myset = (LinearLayout) root.findViewById(R.id.myset);
	myset.setOnClickListener(this);
	img = (ImageView) root.findViewById(R.id.touxiang);
	img.setOnClickListener(this);
	login_bt = (Button) root.findViewById(R.id.btnlogin);
	// 如果用户已经登陆
	if (login) {
	    login_bt.setText("注销");
	    String Url = PreferenceManager.getDefaultSharedPreferences(
		    getContext()).getString("user_picurl", "");
	    if (!TextUtils.isEmpty(Url)) {

		ImageUtils.getInstence().setQueue(getContext());
		ImageUtils.getInstence().loadImageUseVolley_ImageLoad(img, Url);
	    } else {
		img.setImageResource(R.drawable.defaultportrait);

	    }

	} else {
	    img.setImageResource(R.drawable.defaultportrait);

	}
	login_bt.setOnClickListener(this);

    }

    @Override
    public void onStart() {
	super.onStart();
	login = PreferenceManager.getDefaultSharedPreferences(getContext())
		.getBoolean(Constants.USER_STATUS, false);
	if (login) {
	    tvcredit.setText(MyApplication.getCredit() + "");
	    tvmoney.setText(MyApplication.getYue() + "");
	}
    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	case R.id.collect:
	    System.out.println("collect");
	    if (login) {
		Intent intent = new Intent(getActivity(), MyselfActivity.class);
		intent.putExtra("go", 1);
		startActivity(intent);
	    }
	    // getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_fragment,
	    // new FragmentCollect()).commit();
	    break;
	case R.id.myset:
	    Intent i = new Intent(getActivity(), MyselfActivity.class);
	    i.putExtra("go", 2);
	    startActivity(i);
	    break;
	case R.id.myorder:
	    if (login) {
		startActivity(new Intent(getActivity(), MyOrderActivity.class));
	    } else {
		Toast.makeText(getContext(), "你还未登录", 0).show();

	    }
	    break;
	case R.id.touxiang:
	    if (listener != null) {
		if (login) {
		    listener.PersonSetting();
		} else {
		    Toast.makeText(getContext(), "你还未登录", 0).show();
		}
	    }
	    break;
	case R.id.btnlogin:
	    if (listener != null) {
		if (login) {
		    listener.logOff();
		} else {
		    listener.loginButtonClicked();
		}
	    }
	    break;
	case R.id.chong_zhi:
	    if (login) {
		showPopupWindow(v);
	    }
	    break;
	case R.id.yijianfankui:
	    Intent yijian = new Intent(getActivity(), MyselfActivity.class);
	    yijian.putExtra("go", 3);
	    startActivity(yijian);
	    break;
	case R.id.guanyu:
	    Intent guanyu = new Intent(getActivity(), MyselfActivity.class);
	    guanyu.putExtra("go", 4);
	    startActivity(guanyu);
	    break;
	default:
	    break;
	}

    }

    public interface onLoginButtonClickedListener {
	void loginButtonClicked();

	void logOff();

	void PersonSetting();
    }

    private onLoginButtonClickedListener listener;

    public void setOnLoginButtonClickedListener(
	    onLoginButtonClickedListener listener) {
	this.listener = listener;
    }

    // 弹出poupupwindow
    private void showPopupWindow(View v) {
	View contentView = LayoutInflater.from(getActivity()).inflate(
		R.layout.chong_zhi, null);
	pouup = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
		LayoutParams.WRAP_CONTENT);
	pouup.setAnimationStyle(android.R.style.Animation_Toast);
	tel = (TextView) contentView.findViewById(R.id.new_kahao);
	mima = (TextView) contentView.findViewById(R.id.new_mima);
	jine = (TextView) contentView.findViewById(R.id.new_jine);

	moneychongzhi = (Button) contentView.findViewById(R.id.new_chongzhi);

	moneychongzhi.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		System.out.println("abc");
		newtel = tel.getText().toString();
		newmima = mima.getText().toString();
		String s = jine.getText().toString();
		System.out.println("-------------------------------" + newtel
			+ " " + newmima + " " + s);
		if (!s.equals("")) {
		    newjine = Double.parseDouble(s);
		} else {
		    newjine = 0;
		}
		if (newtel.isEmpty()) {
		    Toast.makeText(getActivity(), "手机号不能为空", 0).show();
		} else if (newmima.isEmpty()) {
		    Toast.makeText(getActivity(), "密码不能为空", 0).show();
		} else if (newjine >= 1) {
		    getxutil();
		} else {
		    Toast.makeText(getActivity(), "金额无效", 0).show();
		}
	    }

	});
	guanbi = (Button) contentView.findViewById(R.id.new_guanbi);

	guanbi.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		pouup.dismiss();

	    }
	});

	// 设置好参数之后再show
	pouup.setFocusable(true);
	pouup.setOutsideTouchable(true);
	pouup.update();
	pouup.setBackgroundDrawable(new BitmapDrawable());

	pouup.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    public void getxutil() {
	HttpUtils http = new HttpUtils();
	RequestParams params = new RequestParams();
	String url = MyPropertiesUtil.getProperties(getActivity()).getProperty(
		"url");
	http.send(HttpMethod.GET, url
		+ "/HiWeek/servlet/client/Chongzhi?newtel=" + newtel
		+ "&newmima=" + newmima + "&newjine=" + newjine + "&u_id="
		+ MyApplication.getU_id() + "&u_yue=" + MyApplication.getYue(),
		params, new RequestCallBack<String>() {

		    @Override
		    public void onFailure(HttpException arg0, String arg1) {
			Toast.makeText(getActivity(), "网络错误", 0).show();
		    }

		    @Override
		    public void onSuccess(ResponseInfo<String> arg0) {
			if (arg0.result.toString().equals("充值成功")) {
			    tvmoney.setText(MyApplication.getYue() + newjine
				    + "");
			    MyApplication.setYue(MyApplication.getYue()
				    + newjine);
			    System.out.println(MyApplication.getYue());

			    Editor editor = PreferenceManager
				    .getDefaultSharedPreferences(getActivity())
				    .edit();

			    editor.putFloat("user_YuE",
				    (float) MyApplication.getYue());

			    editor.apply();
			}
			Toast.makeText(getActivity(), arg0.result.toString(), 0)
				.show();
			pouup.dismiss();
		    }

		});

    }
}
