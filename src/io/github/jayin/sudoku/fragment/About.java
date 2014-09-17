package io.github.jayin.sudoku.fragment;

import io.github.jayin.sudoku.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class About extends BaseFragment {

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragemnt_about, container,false);
		return v;
	}
}
