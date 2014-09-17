package io.github.jayin.sudoku.fragment;

import io.github.jayin.sudoku.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Game extends BaseFragment {
	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_game, container, false);
		return v;
	}

}
