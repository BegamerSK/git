package com.sk.nerdlauncher;

import android.support.v4.app.Fragment;

public class MainActivity extends SimpleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new LauncherFragment();
	}


}
