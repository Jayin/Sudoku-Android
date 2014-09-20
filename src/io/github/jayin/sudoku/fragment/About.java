package io.github.jayin.sudoku.fragment;

import io.github.jayin.sudoku.R;
import io.github.jayin.sudoku.common.AndroidUtils;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class About extends BaseFragment {

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragemnt_about, container,false);
		try {
			((TextView)getView(v, R.id.tv_version)).setText(AndroidUtils.getAppVersionName(getContext()));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return v;
	}
}
