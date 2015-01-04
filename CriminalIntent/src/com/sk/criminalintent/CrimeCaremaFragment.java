package com.sk.criminalintent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCaremaFragment extends Fragment {
	private static final String TAG = "CrimeCaremaFragment";
	private Camera camera;
	private SurfaceView surfaceView;
	
	private View progressContainer;
	
	public static final String EXTRA__PHOTO_FILENAME = "com.sk.criminalintent.photo_filename";
	
	
	private ShutterCallback shutterCallback = new ShutterCallback() {
		
		@Override
		public void onShutter() {
			progressContainer.setVisibility(View.VISIBLE);
		}
	};
	
	private  PictureCallback jpegCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			String filename = UUID.randomUUID().toString()+".jpg";
			
			FileOutputStream fos = null;
			boolean success = true;
			
			try {
				fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				fos.write(data);
			} catch (Exception e) {
				Log.e(TAG, "Error writing to file "+filename, e);
				success = false;
			}finally{
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						Log.e(TAG, "Error closing file "+filename, e);
						success = false;
					}
				}
			}
			
			if(success){
				Log.i(TAG, "JPEG saved at "+filename);
				Intent i = new Intent();
				i.putExtra(EXTRA__PHOTO_FILENAME, filename);
				getActivity().setResult(Activity.RESULT_OK, i);
			}else{
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();
		}
	};
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_camera, container,false);
		progressContainer = v.findViewById(R.id.crime_camera_progressContainer);
		progressContainer.setVisibility(View.INVISIBLE);
		
		Button takePicturnButton = (Button) v.findViewById(R.id.crime_camera_takePictureButton);
		takePicturnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//getActivity().fileList();
				if(camera!=null){
					camera.takePicture(shutterCallback, null, jpegCallback);
				}
				
			}
		});
		surfaceView = (SurfaceView) v.findViewById(R.id.crime_camera_surfaceView);
		
		SurfaceHolder holder = surfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if(camera!=null){
					camera.stopPreview();
				}
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if(camera!=null){
					try {
						camera.setPreviewDisplay(holder);
					} catch (IOException e) {
						Log.e(TAG, "Error setting up preview display", e);
					}
				}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				if(camera==null){
					return;
				}
				Camera.Parameters parameters = camera.getParameters();
				Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPreviewSize(s.width, s.height);
				
				s = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPictureSize(s.width, s.height);
				
				camera.setParameters(parameters);
				
				try {
					camera.startPreview();
				} catch (Exception e) {
					camera.release();
					camera = null;
				}
				
				
			}
		});
		
		
		return v;
	}
	
	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
			camera = Camera.open(0);
		}else{
			camera = Camera.open();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(camera!=null){
			camera.release();
			camera = null;
		}
	}
	
	
	private Size getBestSupportedSize(List<Size> sizes,int width,int height){
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width*bestSize.height;
		for(Size s:sizes){
			int area = s.width*s.height;
			if(area>largestArea){
				bestSize = s;
				largestArea = area;
			}
		}
		return bestSize;
	}
	
	
}
