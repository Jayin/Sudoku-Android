package io.github.jayin.sudoku;


import io.github.jayin.sudoku.fragment.About;
import io.github.jayin.sudoku.fragment.Introduction;
import io.github.jayin.sudoku.fragment.SelectLevel;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class Main extends BaseActivity implements View.OnClickListener {
	private DrawerLayout mDrawerLayout;
	private ViewGroup mDrawerLeft;
	private ActionBarDrawerToggle mDrawerToggle;
	private ActionBar mActionBar;
	private long record_time;
	private FragmentManager mFragmentManager;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_main);

		mFragmentManager = getSupportFragmentManager();

		mDrawerLeft = getView(R.id.drawer_left);

		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);

		// DrawerLayout
		mDrawerLayout = getView(R.id.drawer_layout);
		mFragmentManager.beginTransaction().add(R.id.container,new SelectLevel()).commit();
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close);
		mDrawerLayout.setDrawerListener(new MainDrawerListener());

		// left drawer
		getView(R.id.btn_about).setOnClickListener(this);
		getView(R.id.btn_introduction).setOnClickListener(this);
		getView(R.id.btn_select).setOnClickListener(this);
	}

	public void setMainFragment(Fragment fragment) {
		mFragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit).replace(R.id.container, fragment)
				.commit();
	}
	
	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.acty_main, menu);
		return true;
	}

	@Override protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mDrawerLayout.isDrawerOpen(mDrawerLeft)) {
				mDrawerLayout.closeDrawers();
				return true;
			}
			if (System.currentTimeMillis() - record_time <= 1000) {
				closeActivity();
			} else {
				record_time = System.currentTimeMillis();
				T("再按一次退出");
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	private class MainDrawerListener implements DrawerListener {

		@Override public void onDrawerOpened(View drawerView) {
			mDrawerToggle.onDrawerOpened(drawerView);
			mActionBar.setTitle(getResources().getString(R.string.app_setting));
		}

		@Override public void onDrawerClosed(View drawerView) {
			mDrawerToggle.onDrawerClosed(drawerView);
			mActionBar.setTitle(getResources().getString(R.string.app_name));
		}

		@Override public void onDrawerSlide(View drawerView, float slideOffset) {
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override public void onDrawerStateChanged(int newState) {
			mDrawerToggle.onDrawerStateChanged(newState);
		}

	}

	@Override public void onClick(View v) {
		mDrawerLayout.closeDrawers();

		switch (v.getId()) {
		case R.id.btn_about:
			setMainFragment(new About());
			break;
		case R.id.btn_introduction:
			setMainFragment(new Introduction());
			break;
		case R.id.btn_select:
			setMainFragment(new SelectLevel());
			break;
		default:
			T("default");
			break;
		}
	}
}
