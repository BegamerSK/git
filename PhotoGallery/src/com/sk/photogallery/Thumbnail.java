package com.sk.photogallery;

import android.os.HandlerThread;
import android.util.Log;

public class Thumbnail<Token> extends HandlerThread {
	private static final String TAG = "Thumbnail";
	
	public Thumbnail(){
		super(TAG);
	}
	
	public void queueThumbnail(Token token,String url){
		Log.i(TAG, "Got an URL: "+url);
	}
}
