package io.github.jayin.sudoku.common;

import io.github.jayin.sudoku.Main;
import io.github.jayin.sudoku.R;
import io.github.jayin.sudoku.core.Sudoku;
import io.github.jayin.sudoku.core.Table;
import io.github.jayin.sudoku.core.Table.PendingNode;

import java.util.ArrayList;
import java.util.List;

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
	int[][] curMatrix = new int[Table.ROW][Table.ROW];
	List<Integer> pendingNumber = new ArrayList<Integer>();

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
	}

	@Override protected void onStart() {
		super.onStart();
		Log.d("debug", "onstart..");
		gv.setAdapter(new SelectAdapter());
	}

	@Override protected void onStop() {
		super.onStop();
		Log.d("debug", "onStop..");
	}

	public void show(int[][] curMatrix, int position) {
		Log.d("debug", "show..");
		super.show();
		this.mPosition = position;
		U.copyMatrix(this.curMatrix, curMatrix);
		this.curMatrix[position / Table.ROW][position % Table.ROW] = 0;
		pendingNumber.clear();
		Sudoku sudoku = new Sudoku();
		PendingNode n = null;
		try {
			n = sudoku.init(this.curMatrix).getPendingNode(
					position / Table.ROW, position % Table.ROW);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (n != null) {
			for(Integer num :n.getPendingList()){
				pendingNumber.add(num);
			}
			Log.d("debug", pendingNumber.toString());
		}
	}

	/**
	 * 
	 * @param position
	 *            第x个点
	 * @param selectedNumber
	 *            选中的数字
	 */
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
			if (pendingNumber != null && pendingNumber.contains(num)) {
				contentView.setOnClickListener(new View.OnClickListener() {

					@Override public void onClick(View arg0) {
						sendSelectMsg(mPosition, position + 1);
					}
				});
			} else {
				contentView.setBackgroundColor(getContext().getResources()
						.getColor(R.color.grey));
				((TextView) contentView.findViewById(R.id.tv_number))
						.setText("X");
				contentView.setOnClickListener(new View.OnClickListener() {

					@Override public void onClick(View arg0) {
						sendSelectMsg(mPosition, 0);
					}
				});
			}
			return contentView;
		}

	}
}
