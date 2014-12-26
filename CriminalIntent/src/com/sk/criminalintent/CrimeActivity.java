package com.sk.criminalintent;

import java.util.UUID;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class CrimeActivity extends SimpleFragmentActivity {


	@Override
	protected Fragment createFragment() {
		UUID id = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		return CrimeFragment.newInstance(id);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.crime, menu);
//		return true;
//	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		System.out.println("CrimeActivity.onActivityResult");
	}

}
