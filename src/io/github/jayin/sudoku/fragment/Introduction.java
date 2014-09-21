package io.github.jayin.sudoku.fragment;

import io.github.jayin.sudoku.R;
import io.github.jayin.sudoku.common.U;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class Introduction extends BaseFragment {
	private ProgressBar pb;
	private ScrollView sv_container;
	private TextView tv_introduce;

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_introduction, container,
				false);
		pb = getView(v, R.id.pb);
		sv_container = getView(v, R.id.sv_container);
		tv_introduce = getView(v, R.id.tv_introduce);
		new LoadIntroduction().execute();
		return v;
	}

	class LoadIntroduction extends AsyncTask<Void, Void, Spanned> {

		@Override protected void onPreExecute() {
			pb.setVisibility(View.VISIBLE);
			sv_container.setVisibility(View.GONE);
		}

		@Override protected void onPostExecute(Spanned result) {
			pb.setVisibility(View.GONE);
			sv_container.setVisibility(View.VISIBLE);
			tv_introduce.setText(result);
		}

		@Override protected Spanned doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String source = U.readFile(getResources().openRawResource(
					R.raw.sudoku_introduce));
			Spanned spanned = Html.fromHtml(source);
			return spanned;
		}

	}
}
