package io.github.jayin.sudoku;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {
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
	
	@SuppressWarnings("unchecked") public <E extends View > E getView(int resId){
		return (E)findViewById(resId);
	}

	/**
	 * close current Activity
	 */
	public void closeActivity() {
		finish();
	}
	
	public Context getContext(){
		return this;
	}
	
	
}
