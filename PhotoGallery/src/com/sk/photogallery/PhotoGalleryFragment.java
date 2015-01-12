package com.sk.photogallery;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoGalleryFragment extends Fragment {
	public static final String TAG = "PhotoGalleryFragment";
	
	GridView mGridView;
	private ArrayList<GalleryItem> items;
	Thumbnail<ImageView> thumbnailThread;
	
	void setupAdapter(){
		if(getActivity()==null||mGridView==null){
			return;
		}
		if(items!=null){
			//mGridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(), android.R.layout.simple_gallery_item, items));
			mGridView.setAdapter(new GalleryItemAdapter(items));
		
		}else{
			mGridView.setAdapter(null);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		new FetchItemsTask().execute();
		
		thumbnailThread = new Thumbnail<ImageView>();
		thumbnailThread.start();
		thumbnailThread.getLooper();
		Log.i(TAG, "Background thread started");
		
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		thumbnailThread.quit();
		Log.i(TAG, "Background thread destroyed");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_photo_gallery, container,false);
		
		mGridView = (GridView) view.findViewById(R.id.gridView);
		
		setupAdapter();
		
		return view;
	}
	
	private class GalleryItemAdapter extends ArrayAdapter<GalleryItem>{

		public GalleryItemAdapter(List<GalleryItem> items) {
			super(getActivity(), 0, items);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView==null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
			}
			ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
			imageView.setImageResource(R.drawable.ic_launcher);
			
			GalleryItem item = getItem(position);
			thumbnailThread.queueThumbnail(imageView, item.getmUrl());
			
			return convertView;
		}
		
	}
	
	private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>>{

		@Override
		protected ArrayList<GalleryItem> doInBackground(Void... params) {
//			String result;
//			try {
//				result = new FlickrFetchr().getUrl("http://121.40.178.253:8080/fantasy_info/gm_index.do");
//				Log.i(TAG, "Fetched connection of URL: "+result);
//			} catch (IOException e) {
//				Log.e(TAG, "Failed to fetch URL: ", e);
//			}
			return new FlickrFetchr().fetchItems();
		}
		
		@Override
		protected void onPostExecute(ArrayList<GalleryItem> result) {
			items = result;
			setupAdapter();
		}
		
	}
}
