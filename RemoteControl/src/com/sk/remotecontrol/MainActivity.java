package com.sk.remotecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;

public class MainActivity extends SimpleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new RemoteFragment();
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
	}


}
