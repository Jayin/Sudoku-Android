package io.github.jayin.sudoku.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BaseFragment extends Fragment {

	@SuppressWarnings("unchecked") public <E extends BaseFragment> E setArgument(Bundle args) {
		this.setArguments(args);
		return (E)this;
	}

	@SuppressWarnings("unchecked") public <E extends View> E getView(View v,
			int resId) {
		return (E) v.findViewById(resId);
	}
	
	public Context getContext(){
		return getActivity();
	}

	public void T(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	public void D(String msg) {
		Log.d("debug",getClass().getSimpleName()+" - "+ msg);
	}

}
