package io.github.jayin.sudoku.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class BaseFragment extends Fragment{
	
	public void T(String msg){
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public void D(String msg){
		Log.d("debug", msg);
	}

}
