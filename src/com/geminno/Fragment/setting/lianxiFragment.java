package com.geminno.Fragment.setting;

import com.geminno.hiweek1_0.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class lianxiFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getActivity().getActionBar().setTitle("联系我们");
	View root = inflater.inflate(R.layout.lianxi, null);

	return root;
    }
}
