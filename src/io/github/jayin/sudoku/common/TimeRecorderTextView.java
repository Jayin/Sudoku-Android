package io.github.jayin.sudoku.common;

import java.util.Date;
import java.util.Formatter;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * tv .clean().startXX() ,tv.stop() ,tv,getCostTIme
 * @author jayin
 *
 */
public class TimeRecorderTextView extends TextView {

	private CoundownHandler mHandler;
	private int mCostTime;
	private Timer mTimer; 
	private Task task;
	private boolean isCounting = false;

	private final int MSG_TIMING = 0;
	private final int MSG_STOP = 1;
	private final int MSG_START = 2;

	public TimeRecorderTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mHandler = new CoundownHandler();
		mTimer = new Timer();
	}

	public TimeRecorderTextView clean() {
		mCostTime = 0;
		if(task != null){
			task.cancel();
		}
		return this;
	}

	public void startTiming() {
		isCounting = true;
		if(task!=null){
			task.cancel();
		}
		task = new Task(mHandler);
		mTimer.schedule(task, new Date(), 1000);
	}

	public void stopTiming() {
		task.cancel();
		mHandler.sendEmptyMessage(MSG_STOP);
	}

	public int getCostTime() {
		return this.mCostTime;
	}
	
	public boolean isTiming(){
		return isCounting;
	}

	private class Task extends TimerTask {
		private CoundownHandler h;
		public Task(CoundownHandler h) {
			this.h = h;
		}

		@Override public void run() {
			h.sendEmptyMessage(MSG_TIMING);
		}
	}

	@SuppressLint("HandlerLeak") private class CoundownHandler extends Handler {

		@Override public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_START:
				mCostTime = 0;
				TimeRecorderTextView.this.setText(new Formatter().format(
						"%2d:%2d:%2d", 0, 0, 0).toString());
				break;
			case MSG_TIMING:
				mCostTime++;

				int hour = mCostTime / (60 * 60);
				int min = (mCostTime - hour * 3600) / (60);
				int sec = mCostTime - hour * 3600 - min * 60;
				TimeRecorderTextView.this.setText(new Formatter().format(
						"%02d:%02d:%02d", hour, min, sec).toString());
				break;
			case MSG_STOP:
				//handler stop
				break;
			default:
				break;
			}
		}

	}
}
