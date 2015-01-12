package com.sk.remotecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RemoteFragment extends Fragment {
	private TextView mSelectTextView;
	private TextView mWorkingTextView;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_remote_control, container, false);
		
		mSelectTextView = (TextView) view.findViewById(R.id.fragment_remote_control_selectedTextView);
		mWorkingTextView = (TextView) view.findViewById(R.id.fragment_remote_control_workingTextView);
		
		View.OnClickListener numberButtonListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView tv = (TextView) v;
				String working = mWorkingTextView.getText().toString();
				String text = tv.getText().toString();
				if(working.equals("0")){
					mWorkingTextView.setText(text);
				}else{
					mWorkingTextView.setText(working+text);
				}
				
			}
		};
		
//		Button zeroButton = (Button) view.findViewById(R.id.fragment_remote_control_zeroButton);
//		zeroButton.setOnClickListener(numberButtonListener);
//		
//		Button oneButton = (Button) view.findViewById(R.id.fragment_remote_control_oneButton);
//		oneButton.setOnClickListener(numberButtonListener);
		TableLayout tableLayout = (TableLayout) view.findViewById(R.id.fragment_remote_control_tableLayout);
		int number = 1;
		for(int i=2;i<tableLayout.getChildCount()-1;i++){
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for(int j=0;j<row.getChildCount();j++){
				Button button = (Button) row.getChildAt(j);
				button.setText(""+number);
				button.setOnClickListener(numberButtonListener);
				number++;
			}
		}
		
		
		TableRow bottomRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount()-1);
		Button deleteButton  = (Button) bottomRow.getChildAt(0);
		deleteButton.setText("Delete");
		deleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWorkingTextView.setText("0");
			}
		});
		
		Button zeroButton = (Button) bottomRow.getChildAt(1);
		zeroButton.setOnClickListener(numberButtonListener);
		zeroButton.setText("0");
		
		Button enterButton = (Button) bottomRow.getChildAt(2);
		enterButton.setText("Enter");
		enterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CharSequence working = mWorkingTextView.getText();
				if(working.length()>0){
					mSelectTextView.setText(working);
				}
				mWorkingTextView.setText("0");
			}
		});
		
		
		return view;
	}
	
}
