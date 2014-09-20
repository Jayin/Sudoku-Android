9.18
===

### Fragment.onCreateOptionsMenu(Menu,MenuInflater)
Framgent能让开发分层更好，目前遇到个需求:当切换不同的Fragment的时候，OptionsMenu 也会跟着Fragment变化.

很自然的写出下面代码
```java
	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.surrender) {
			//handle this
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
```

然后你会发现压根不起作用.原因就是没有告诉系统,我这个Fragment修改了OptionsMenu,得刷新一下
看了Android官方的demo，优雅地写出几行代码
```java
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ......
		// notify the system to refresh the option menu
		setHasOptionsMenu(true);
	}
```

9.19
===

在玩Sudoku的时候，点击空白框，然后选择数字，接着数字就会填到表中(GridView)

一开始我的做法是Game Fragment onCreate()中注册一个BroadcastReceiver(在onDestroy中注销),然后Select Dialog中选着后就发广播

但是，这根本不起作用！
我的最后方案是，在Select Dialog 中设置回调接口，才发现，这才是最合理？

至于为啥不行？onActivityAttch

