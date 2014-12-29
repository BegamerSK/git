package com.sk.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class CrimeCaremaAcivity extends SimpleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CrimeCaremaFragment();
	}

	
	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(arg0);
	}
}
