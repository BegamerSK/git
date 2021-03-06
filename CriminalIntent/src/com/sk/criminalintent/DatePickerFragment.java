package com.sk.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {
	
	public static final String EXTRA_DATE = "com.sk.dpf.date";
	private Date mDate;
	
	private void sendResult(int resultCode){
		if(getTargetFragment()==null){
			return;
		}
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		
		
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		
		DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
		datePicker.init(year, month, day, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
				getArguments().putSerializable(EXTRA_DATE, mDate);
				System.out.println("date changed~~~~~~~~~~~");
			}
		});
		
		return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title)
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendResult(Activity.RESULT_OK);
						
					}
				})
				.setView(v)
				.setTitle(R.string.date_picker_title)
				.create();
	}
	
	public static DatePickerFragment newInstance(Date date){
		Bundle bundel = new Bundle();
		bundel.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(bundel);
		return fragment;
	}
}
