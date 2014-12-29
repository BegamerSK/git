package com.sk.criminalintent;

import java.io.IOException;
import java.util.List;

import android.annotation.TargetApi;
import android.hardware.Camera;
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
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_camera, container,false);
		Button takePicturnButton = (Button) v.findViewById(R.id.crime_camera_takePictureButton);
		takePicturnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().fileList();
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
