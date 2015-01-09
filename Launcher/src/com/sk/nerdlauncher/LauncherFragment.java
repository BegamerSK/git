package com.sk.nerdlauncher;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LauncherFragment extends ListFragment {
	
	private static final String TAG = "LauncherFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent startupIntent = new Intent(Intent.ACTION_MAIN);
		startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		PackageManager pm = getActivity().getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
		
		Log.i(TAG, "I've found "+activities.size()+" activities");
		
		Collections.sort(activities,new Comparator<ResolveInfo>() {

			@Override
			public int compare(ResolveInfo a, ResolveInfo b) {
				PackageManager pm = getActivity().getPackageManager();
				return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(), b.loadLabel(pm).toString());
			}
		});
		
		ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(getActivity(), android.R.layout.simple_list_item_1,activities){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				PackageManager pm = getActivity().getPackageManager();
				TextView tv = (TextView) view;
				ResolveInfo info = getItem(position);
				tv.setText(info.loadLabel(pm));
				
				return view;
			}
		};
		
		
		setListAdapter(adapter);
		
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ResolveInfo info = (ResolveInfo) l.getAdapter().getItem(position);
		ActivityInfo actvityInfo = info.activityInfo;
		
		if(actvityInfo==null){
			return;
		}
		
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setClassName(actvityInfo.applicationInfo.packageName, actvityInfo.name);
		System.out.println(actvityInfo.applicationInfo.packageName+"    "+actvityInfo.name);
		
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		
	}
	
	

}
