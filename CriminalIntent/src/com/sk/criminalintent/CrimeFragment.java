package com.sk.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CrimeFragment extends Fragment {
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	private ImageView photoView;
	
	private ImageButton photoButton;
	public static final String TAG = "CrimeFragment";
	public static final String EXTRA_CRIME_ID = "com.sk.crime.id";
	private static final String DIALOG_DATE = "com.sk.date";
	private static final String DIALOG_IMAGE = "com.sk.image";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_PHOTO = 1;
	
	
	public static CrimeFragment newInstance(UUID id){
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_CRIME_ID, id);
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	private void showPhoto(){
		Photo p = mCrime.getPhoto();
		BitmapDrawable b = null;
		if(p!=null){
			String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		photoView.setImageDrawable(b);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(photoView);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
		case android.R.id.home:
			if(NavUtils.getParentActivityName(getActivity())!=null){
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		System.out.println("Crimes is saved!!!!");
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//UUID id = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		UUID id = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(id);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=Activity.RESULT_OK){
			return;
		}
		if(requestCode==REQUEST_DATE){//回来更新修改后的日期啊
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setmDate(date);
			mDateButton.setText(DateFormat.format("yyyy-MM-dd hh:mm:ssaa", mCrime.getmDate()).toString());
		}else if(requestCode==REQUEST_PHOTO){
			String filename = data.getStringExtra(CrimeCaremaFragment.EXTRA__PHOTO_FILENAME);
			if(filename!=null){
				//Log.i(TAG, "filename: "+filename);
				Photo p = new Photo(filename);
				mCrime.setPhoto(p);
				showPhoto();
				//Log.i(TAG, "Crime: "+mCrime.getmTitle()+" has a photo");
			}
		}
	}
	
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crime, container,false);
		
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB&&NavUtils.getParentActivityName(getActivity())!=null){
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		mTitleField = (EditText) view.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getmTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCrime.setmTitle(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		mDateButton = (Button) view.findViewById(R.id.crime_date);
		mDateButton.setText(DateFormat.format("yyyy-MM-dd hh:mm:ssaa", mCrime.getmDate()).toString());
		mDateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.ismSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setmSolved(isChecked);
				
			}
		});
		
		photoButton = (ImageButton) view.findViewById(R.id.crime_imageButton);
		photoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),CrimeCaremaAcivity.class);
				//startActivity(i);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		});
		
		photoView = (ImageView) view.findViewById(R.id.crime_imageView);
		photoView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Photo p = mCrime.getPhoto();
				if(p==null){
					return;
				}
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			}
		});
		
		
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)||
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)||
				Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD||
				Camera.getNumberOfCameras()>0;
				
		if(!hasACamera){
			photoButton.setEnabled(false);
		}
		
		
		return view;
	}
}
