package com.sk.photogallery;

import android.support.v4.app.Fragment;

public class MainActivity extends SimpleFragmentActivity {


	@Override
	protected Fragment createFragment() {
		return new PhotoGalleryFragment();
	}

}
