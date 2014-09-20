package io.github.jayin.sudoku.fragment;

import io.github.jayin.sudoku.R;
import io.github.jayin.sudoku.common.AndroidUtils;
import io.github.jayin.sudoku.common.LoadDialog;
import io.github.jayin.sudoku.common.SelectDialog;
import io.github.jayin.sudoku.common.SelectDialog.onNumberSelectListener;
import io.github.jayin.sudoku.common.TimeRecorderTextView;
import io.github.jayin.sudoku.common.U;
import io.github.jayin.sudoku.core.Sudoku;
import io.github.jayin.sudoku.core.Table;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class Game extends BaseFragment implements onNumberSelectListener {
	private int lv, num;
	private MediaPlayer mMediaPlayer;
	GridView gv;
	GvAdapter adapter;
	SelectDialog selectDialog;
	LoadDialog loadDialog;
	TimeRecorderTextView timeRecorder;
	long record_time = 0;
	int[][] rawMatrix = new int[Table.ROW][Table.ROW];
	int[][] curMatrix = new int[Table.ROW][Table.ROW];

	public static Game newInstance(int lv, int num) {
		Bundle args = new Bundle();
		args.putInt("lv", lv);
		args.putInt("num", num);
		return new Game().setArgument(args);
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			lv = getArguments().getInt("lv", 1);
			num = getArguments().getInt("num", 1);
		} else {
			lv = num = 1;
		}
		// 通知系统刷新这里的OptionMenu
		setHasOptionsMenu(true);

	}

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_game, container, false);
		selectDialog = new SelectDialog(getActivity());
		selectDialog.setOnNumberSelectListener(this);

		loadDialog = new LoadDialog(getActivity());
		U.copyMatrix(rawMatrix, U.readMatrix(getContext(), lv, num));
		U.copyMatrix(curMatrix, rawMatrix);
		gv = getView(v, R.id.gv);
		timeRecorder = getView(v, R.id.tv_timerecord);

		timeRecorder.clean();
		adapter = new GvAdapter(getContext());
		gv.setAdapter(adapter);

		return v;
	}

	private void share() {
		U.screenShot(getView());
		String text = "这个数独我耗时 " + timeRecorder.getText()
				+ " !不服?想挑战我?赶紧下载来玩玩吧！";

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.setType("image/*");
		sendIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.fromFile(new File(U.getScreenShotPath())));
		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.putExtra("Kdescription", text);
		startActivity(sendIntent);
	}

	@Override public void onStart() {
		super.onStart();
		timeRecorder.startTiming();
	}

	@Override public void onPause() {
		super.onPause();
		timeRecorder.stopTiming();
	}

	@Override public void onDestroy() {
		super.onDestroy();
	}

	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.fragment_game, menu);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.surrender) {
			T("鄙视你,居然认输..");
			timeRecorder.clean();
			new SolveTask().execute();
			return true;
		}else if(item.getItemId() == R.id.tips){
			gv.getChildAt(12).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out_in));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean checkMatrix() {
		Sudoku sudoku = new Sudoku();
		if (sudoku.check(curMatrix)) {
			AlertDialog dialog = new AlertDialog.Builder(getContext())
					.setTitle("恭喜完成数独! 耗时:" + timeRecorder.getCostTime() + "s")
					.setCancelable(true)
					.setNegativeButton("分享",
							new DialogInterface.OnClickListener() {

								@Override public void onClick(
										DialogInterface arg0, int arg1) {
									share();
								}
							}).create();
			dialog.show();
			timeRecorder.stopTiming();
			return true;
		}
		return false;
	}

	@Override public void onSelect(int position, int number) {
		try {
			curMatrix[position / Table.ROW][position % Table.ROW] = number;
			new Sudoku().init(curMatrix);
			adapter.notifyDataSetInvalidated();
			if(checkMatrix()){
				musicFinish();
			}else{
				musicAddNumber();
			}
		} catch (Exception e) {
			e.printStackTrace();
			curMatrix[position / Table.ROW][position % Table.ROW] = 0;
			adapter.notifyDataSetInvalidated();
			T("发生冲突啦!");
		}
	}

	public void playMusic(int resId) {
		mMediaPlayer = MediaPlayer.create(getContext(), resId);
		try {
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override public void onCompletion(MediaPlayer mp) {
					mp.release();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void musicAddNumber() {
		playMusic(R.raw.voice_add_number);
	}

	public void musicFinish() {
		playMusic(R.raw.voice_finish);
	}

	class GvAdapter extends BaseAdapter {

		Context context;

		public GvAdapter(Context context) {
			this.context = context;
		}

		@Override public int getCount() {
			return Table.ROW * Table.ROW;
		}

		@Override public Object getItem(int position) {
			return curMatrix[position / Table.ROW][position % Table.ROW];
		}

		@Override public long getItemId(int position) {
			return position;
		}

		@Override public View getView(final int position, View contentView,
				ViewGroup parent) {
			ViewHolder h = null;
			if (contentView == null) {
				h = new ViewHolder();
				contentView = LayoutInflater.from(context).inflate(
						R.layout.item_sudoku, null);
				h.tv_number = (TextView) contentView
						.findViewById(R.id.tv_number);
				h.line_top = contentView.findViewById(R.id.line_top);
				h.line_left = contentView.findViewById(R.id.line_left);
				h.line_right = contentView.findViewById(R.id.line_right);
				h.line_bottom = contentView.findViewById(R.id.line_bottom);
				// final int _p = position;

				contentView.setTag(h);
			} else {
				h = (ViewHolder) contentView.getTag();
			}
			int row = position / Table.ROW;
			int column = position % Table.ROW;
			// background
			if (curMatrix[row][column] == 0) {
				h.tv_number.setText("");
				h.tv_number.setBackgroundColor(context.getResources().getColor(
						android.R.color.white));
			} else {
				h.tv_number.setText("" + curMatrix[row][column]);
				if (rawMatrix[row][column] != 0) {
					h.tv_number.setBackgroundColor(context.getResources()
							.getColor(R.color.grey));
				} else {
					h.tv_number.setBackgroundColor(context.getResources()
							.getColor(android.R.color.white));
				}
			}

			// line
			if ((row + 1) % 3 != 0) {
				h.line_bottom.setVisibility(View.GONE);
			} else {
				h.line_bottom.setVisibility(View.VISIBLE);
			}
			if ((column + 1) % 3 != 0) {
				h.line_right.setVisibility(View.GONE);
			} else {
				h.line_right.setVisibility(View.VISIBLE);
			}
			if (rawMatrix[row][column] == 0) {
				contentView.setOnClickListener(new OnClickListener() {

					@Override public void onClick(View arg0) {
						selectDialog.show(curMatrix, position);
					}
				});
			}
			return contentView;
		}

		class ViewHolder {
			TextView tv_number;
			View line_top, line_left, line_right, line_bottom;
		}
	}

	class SolveTask extends AsyncTask<Void, Void, Void> {
		@Override protected void onPreExecute() {
			loadDialog.show();
		}

		@Override protected void onPostExecute(Void result) {
			adapter = new GvAdapter(getActivity());
			gv.setAdapter(adapter);
			loadDialog.dismiss();
		}

		@Override protected Void doInBackground(Void... arg0) {
			Sudoku sudoku = new Sudoku(true);
			try {
				sudoku.init(rawMatrix).solve();
				int[][] _Matrix = sudoku.getMatrix();
				U.copyMatrix(curMatrix, _Matrix);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
