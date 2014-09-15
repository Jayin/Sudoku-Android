package io.github.jayin.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends Activity {
	/**
	 * toast a string
	 * 
	 * @param msg
	 */
	public void T(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Log a message
	 * 
	 * @param msg
	 */
	public void D(String msg) {
		Log.d("debug", getClass().getSimpleName() + " - " + msg);
	}

	/**
	 * Open a Activity
	 * 
	 * @param cls
	 *            the activity
	 */
	public void openActivity(Class<?> cls) {
		startActivity(new Intent(this, cls));
	}

	/**
	 * close current Activity
	 */
	public void closeActivity() {
		finish();
	}
}
