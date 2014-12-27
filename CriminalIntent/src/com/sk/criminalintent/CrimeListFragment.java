package com.sk.criminalintent;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {

	private ArrayList<Crime> mCrimes;
	
	private static final String TAG = "CrimeListFragment";
	private static final int REQUEST_CRIME = 1;
	private boolean showSubtitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		showSubtitle = false;
		getActivity().setTitle(R.string.crime_title);
		
		mCrimes = CrimeLab.get(getActivity()).getmCrimes();
		
		setRetainInstance(true);
		
//		View empty = getActivity().findViewById(R.layout.)
		//getListView().setEmptyView(getActivity().getLayoutInflater().inflate(R.layout.empty, null));
		
		//ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		
		setListAdapter(adapter);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = super.onCreateView(inflater, container, savedInstanceState);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			if(showSubtitle){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = (Crime) getListAdapter().getItem(position);
		Log.d(TAG, c.getmTitle()+" was clicked");
		
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getmId());
		startActivityForResult(i, REQUEST_CRIME);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==REQUEST_CRIME){
			System.out.println("处理返回啦");
		}
		if(requestCode==0){
			System.out.println("打开新建后的返回呢");
		}
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		
		MenuItem ishowSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if(showSubtitle&&ishowSubtitle!=null){
			ishowSubtitle.setTitle(R.string.hide_subtitle);
		}
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.menu_item_new_crime:
			Crime crime = new Crime();
			CrimeLab.get(getActivity()).addCrime(crime);
			Intent i = new Intent(getActivity(),CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getmId());
			startActivityForResult(i, 0);
			return true;
		case R.id.menu_item_show_subtitle:
			if(getActivity().getActionBar().getSubtitle()==null){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				item.setTitle(R.string.hide_subtitle);
				showSubtitle = true;
			}else{
				getActivity().getActionBar().setSubtitle(null);
				item.setTitle(R.string.show_subtitle);
				showSubtitle = false;
			}
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	private class CrimeAdapter extends ArrayAdapter<Crime>{

		public CrimeAdapter(List<Crime> crimes) {
			super(getActivity(), 0, crimes);
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView==null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
			}
			Crime c = getItem(position);
			TextView title = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
			title.setText(c.getmTitle());
			
			TextView date = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
			date.setText(DateFormat.format("yyyy-MM-dd hh:mm:ssaa", c.getmDate()).toString());
			//date.setText(c.getmDate().toString());
			CheckBox solved = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solved.setChecked(c.ismSolved());
			return convertView;
		}
		
	}
	
	
}
