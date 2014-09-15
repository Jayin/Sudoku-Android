package io.github.jayin.sudoku.common;

import io.github.jayin.sudoku.Main;
import io.github.jayin.sudoku.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class SelectDialog extends Dialog {
	private int mPosition;

	public SelectDialog(Context context) {
		this(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
	}

	public SelectDialog(Context context, int theme) {
		super(context, theme);
	}

	private int[][] numbers = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };

	private GridView gv;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_select);

		gv = (GridView) findViewById(R.id.gv);
		gv.setAdapter(new SelectAdapter());

	}

	@Override protected void onStart() {
		super.onStart();
		Log.d("debug", "onstart..");
	}

	@Override protected void onStop() {
		super.onStop();
		Log.d("debug", "onStop..");
	}

	public void show(int position) {
		super.show();
		mPosition = position;
	}

	private void sendSelectMsg(int position, int selectedNumber) {
		Intent intent = new Intent(Main.ACTION);
		intent.putExtra("position", position);
		intent.putExtra("selected", selectedNumber);
		getContext().sendBroadcast(intent);
		this.dismiss();
	}

	class SelectAdapter extends BaseAdapter {

		@Override public int getCount() {
			return 9;
		}

		@Override public Object getItem(int position) {
			return numbers[position / 3][position % 3];
		}

		@Override public long getItemId(int position) {
			return position;
		}

		@Override public View getView(final int position, View contentView,
				ViewGroup parent) {
			contentView = getLayoutInflater().inflate(
					R.layout.item_select_number, null);
			final int num = numbers[position / 3][position % 3];
			((TextView) contentView.findViewById(R.id.tv_number)).setText(""
					+ num);
			contentView.setOnClickListener(new View.OnClickListener() {

				@Override public void onClick(View arg0) {
					sendSelectMsg(mPosition, position + 1);
				}
			});
			return contentView;
		}

	}
}
