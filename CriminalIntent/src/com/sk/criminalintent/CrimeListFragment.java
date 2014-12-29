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
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

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
		
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
			registerForContextMenu(listView);
		}else{
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
					return false;
				}
				
				@Override
				public void onDestroyActionMode(ActionMode arg0) {
					
				}
				
				@Override
				public boolean onCreateActionMode(ActionMode arg0, Menu arg1) {
					MenuInflater inflater = arg0.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, arg1);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode arg0, MenuItem arg1) {
					switch(arg1.getItemId()){
					case R.id.menu_item_delete_crime:
						CrimeAdapter adpter = (CrimeAdapter) getListAdapter();
						
						for(int i=adpter.getCount();i>=0;i--){
							if(getListView().isItemChecked(i)){
								CrimeLab.get(getActivity()).deleteCrime(adpter.getItem(i));
							}
						}
						arg0.finish();
						adpter.notifyDataSetChanged();
						return true;
						default:
							return false;
					}
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position,
						long id, boolean checked) {
					
				}
			});
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
		Crime crime = adapter.getItem(position);
		switch(item.getItemId()){
		case R.id.menu_item_delete_crime:
			CrimeLab.get(getActivity()).deleteCrime(crime);
			adapter.notifyDataSetChanged();
			return true;
		}
		
		return super.onContextItemSelected(item);
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
