package com.geminno.Fragment.setting;

import com.geminno.hiweek1_0.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FragYiJian extends Fragment {
    EditText et;

    public FragYiJian() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getActivity().getActionBar().setTitle("意见反馈");
	View root = inflater.inflate(R.layout.yijianfankui, null);
	et = (EditText) root.findViewById(R.id.yi_jian_editText1);
	Button btn = (Button) root.findViewById(R.id.yi_jian_button1);
	btn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (!TextUtils.isEmpty(et.getText())) {
		    getActivity().finish();
		} else {
		    Toast.makeText(getActivity(), "内容不能为空", 0).show();
		}
	    }
	});
	return root;
    }
}
