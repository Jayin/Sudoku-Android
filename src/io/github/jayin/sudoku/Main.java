package io.github.jayin.sudoku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.github.jayin.sudoku.common.LoadDialog;
import io.github.jayin.sudoku.common.SelectDialog;
import io.github.jayin.sudoku.core.Sudoku;
import io.github.jayin.sudoku.core.Table;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends BaseActivity {
	public static final String ACTION = "Main";
	MainBroadcastReceiver recevier;
	GridView gv;
	GvAdapter adapter;
	SelectDialog selectDialog;
	LoadDialog loadDialog;
	long record_time = 0;

	int[][] cur_Matrix = new int[Table.ROW][Table.ROW];

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_main);
		selectDialog = new SelectDialog(this);
		loadDialog = new LoadDialog(this);
		getMatrix();
		gv = (GridView) findViewById(R.id.gv);

		adapter = new GvAdapter(this);
		gv.setAdapter(adapter);

		recevier = new MainBroadcastReceiver();
		registerReceiver(recevier, new IntentFilter(ACTION));
	}

	private void getMatrix() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(getAssets().open(
					"data.txt")));
			String lineString = null;
			int line = 0;
			while ((lineString = br.readLine()) != null) {
				String[] s = lineString.split(" ");
				for (int j = 0; j < s.length; j++) {
					cur_Matrix[line][j] = Integer.parseInt(s[j]);
				}
				line++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		if (recevier != null) {
			unregisterReceiver(recevier);
		}
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.acty_main, menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.surrender) {
			// TODO 显示答案..
			T("鄙视你,居然认输..");
			new SolveTask().execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - record_time <= 1000) {
				closeActivity();
			} else {
				record_time = System.currentTimeMillis();
				T("再按一次退出");
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	class MainBroadcastReceiver extends BroadcastReceiver {

		@Override public void onReceive(Context conent, Intent intent) {
			int position = intent.getIntExtra("position", 1);
			int select = intent.getIntExtra("selected", 1);
			Toast.makeText(Main.this, "you select " + select,
					Toast.LENGTH_SHORT).show();
			cur_Matrix[position / Table.ROW][position % Table.ROW] = select;
			adapter.notifyDataSetInvalidated();
		}
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
			return cur_Matrix[position / Table.ROW][position % Table.ROW];
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
				contentView.setOnClickListener(new OnClickListener() {

					@Override public void onClick(View arg0) {
						selectDialog.show(position);

					}
				});
				contentView.setTag(h);
			} else {
				h = (ViewHolder) contentView.getTag();
			}
			int row = position / Table.ROW;
			int column = position % Table.ROW;
			// background
			if (cur_Matrix[row][column] == 0) {
				h.tv_number.setText("");
			} else {
				h.tv_number.setText("" + cur_Matrix[row][column]);
				h.tv_number.setBackgroundColor(context.getResources().getColor(
						R.color.grey));
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
			adapter.notifyDataSetInvalidated();
			loadDialog.dismiss();
		}

		@Override protected Void doInBackground(Void... arg0) {
			Sudoku sudoku = new Sudoku();
			sudoku.init(cur_Matrix).solve();
			int[][] matrix = sudoku.getMatrix();
			for(int i=0;i<Table.ROW;i++)
				for(int j=0;j<Table.ROW;j++)
					cur_Matrix[i][j] = matrix[i][j];
			return null;
		}

	}
}
