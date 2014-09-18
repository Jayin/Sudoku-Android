package io.github.jayin.sudoku.fragment;

import io.github.jayin.sudoku.Main;
import io.github.jayin.sudoku.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectSudoku extends BaseFragment implements View.OnClickListener {

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_selectsudoku, container,
				false);
		getView(v, R.id.btn_lv1).setOnClickListener(this);
		getView(v, R.id.btn_lv2).setOnClickListener(this);
		getView(v, R.id.btn_lv3).setOnClickListener(this);
		getView(v, R.id.btn_lv4).setOnClickListener(this);
		return v;
	}

	@Override public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_lv1:
			((Main)getActivity()).setMainFragment(Game.newInstance(1, 2));
			
			break;
		case R.id.btn_lv2:
			((Main)getActivity()).setMainFragment(Game.newInstance(2, 2));
			break;
		case R.id.btn_lv3:
			((Main)getActivity()).setMainFragment(Game.newInstance(3, 2));
			break;
		case R.id.btn_lv4:
			((Main)getActivity()).setMainFragment(Game.newInstance(4, 2));
			break;
		default:
			break;
		}
	}

}
