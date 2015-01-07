package com.sk.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {

	
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity a,String path){
		Display display = a.getWindowManager().getDefaultDisplay();
		float destWith = display.getWidth();
		float destHeight = display.getHeight();
		
		System.out.println("destWith: "+destWith+"   destHeight:"+destHeight);
		
		
		BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		System.out.println("srcWidth: "+srcWidth+"   srcHeight:"+srcHeight);
		int inSampleSize = 1;
		if(srcHeight>destHeight||srcWidth>destWith){
			System.out.println("PictureUtils   比大");
			if(srcWidth>srcHeight){
				inSampleSize = Math.round(srcHeight/destHeight);
			}else{
				inSampleSize = Math.round(srcWidth/destWith);
			}
		}else{
			System.out.println("PictureUtils   不比大");
		}
		System.out.println("inSampleSize: "+inSampleSize);
//		inSampleSize = 32;
//		System.out.println("after inSampleSize: "+inSampleSize);
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return new BitmapDrawable(a.getResources(), bitmap);
	}
	
	public static void cleanImageView(ImageView imageView){
		if(!(imageView.getDrawable() instanceof BitmapDrawable)){
			return;
		}
		BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
		b.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}
	
	
}
