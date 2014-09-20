package io.github.jayin.sudoku.fragment;

import io.github.jayin.sudoku.R;
import io.github.jayin.sudoku.common.U;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Introduction extends BaseFragment {
	
	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_introduction, container, false);
		String source = U.readFile(getResources().openRawResource(R.raw.sudoku_introduce));
		Spanned spanned = Html.fromHtml(source);
		System.out.println(spanned.toString());
		((TextView)v.findViewById(R.id.tv_introduce)).setText(spanned);
		return v;
	}

}
