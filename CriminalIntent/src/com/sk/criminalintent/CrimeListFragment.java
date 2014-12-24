package com.sk.criminalintent;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.crime_title);
		
		mCrimes = CrimeLab.get(getActivity()).getmCrimes();
		
		
		
		//ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		
		setListAdapter(adapter);
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
