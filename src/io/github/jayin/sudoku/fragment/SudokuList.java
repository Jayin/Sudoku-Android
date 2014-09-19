package io.github.jayin.sudoku.fragment;

import io.github.jayin.sudoku.Main;
import io.github.jayin.sudoku.R;
import io.github.jayin.sudoku.common.U;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SudokuList extends BaseFragment {
	private int lv;
	private int size = 1;
	private ListView listView;
	private SudokuListAdapter adapter;

	public static SudokuList newInstance(int lv) {
		Bundle args = new Bundle();
		args.putInt("lv", lv);
		return new SudokuList().setArgument(args);
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			lv = getArguments().getInt("lv");
		} else {
			lv = 1;
		}
	}

	@Override public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_sudokulist, container,
				false);
		try {
			JSONObject jsonObject = new JSONObject(U.readJsonFile(getContext(), lv));
			size = jsonObject.getInt("size");
		} catch (JSONException e) {
			e.printStackTrace();
			size = 1;
		}
		D("size="+size+" lv="+lv);
		listView  =getView(v, R.id.lv_sudokulist);
		listView.setAdapter(new SudokuListAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 ((Main)getActivity()).setMainFragment(Game.newInstance(lv, position+1));
			}
		});
		return v;
	}
	
	private class SudokuListAdapter extends BaseAdapter{

		@Override public int getCount() {
		 
			return size;
		}

		@Override public Object getItem(int position) {
		 
			return position;
		}

		@Override public long getItemId(int position) {
			 
			return position;
		}

		@Override public View getView(int position, View convertView,
				ViewGroup parent) {
			ViewHodler h = null;
			if(convertView==null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sudokulist, null);
				h = new ViewHodler();
				h.tv_num  = (TextView)convertView.findViewById(R.id.tv_num);
				convertView.setTag(h);
			}else{
				h = (ViewHodler)convertView.getTag();
			}
			
			h.tv_num.setText("No."+(position+1));
			return convertView;
		}
		
		
		class ViewHodler {
			TextView tv_num;
		}
	}

}





