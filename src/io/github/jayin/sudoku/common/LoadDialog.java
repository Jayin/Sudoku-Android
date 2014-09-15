package io.github.jayin.sudoku.common;

import io.github.jayin.sudoku.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class LoadDialog extends Dialog {
	public LoadDialog(Context context) {
		this(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
	}

	public LoadDialog(Context context, int theme) {
		super(context, theme);
	}
	
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_loading);
		setCancelable(false);
	}
}
