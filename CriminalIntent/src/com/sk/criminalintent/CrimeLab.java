package com.sk.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class CrimeLab {
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	
	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crimes.json";
	
	private ArrayList<Crime> mCrimes;
	
	private CriminalIntentJSONSerializer mSerializer;
	
	
	
	
	
	
	public void addCrime(Crime c){
		mCrimes.add(c);
	}
	
	public Context getmAppContext() {
		return mAppContext;
	}

	
	
	public ArrayList<Crime> getmCrimes() {
		return mCrimes;
	}

	private CrimeLab(Context appContext){
		mAppContext = appContext;
		//mCrimes = new ArrayList<Crime>();
		
		mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
		
		try {
			mCrimes = mSerializer.loadCrimes();
		} catch (Exception e) {
			mCrimes = new ArrayList<Crime>();
			Log.e(TAG, "Error loading crimes: ", e);
		} 
//		for(int i=0;i<100;i++){
//			Crime c = new Crime();
//			c.setmTitle("Crime #"+i);
//			c.setmSolved(i%2==0);
//			mCrimes.add(c);
//		}
	}
	
	public static CrimeLab get(Context c){
		if(sCrimeLab==null){
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	
	public Crime getCrime(UUID id){
		for(Crime c:mCrimes){
			if(c.getmId().equals(id)){
				return c;
			}
		}
		return null;
	}
	
	public boolean saveCrimes(){
		try {
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "crimes saved to file");
			return true;
		} catch (Exception e) {
			Log.e(TAG,"Error saving crimes: ", e);
			return false;
		}
	}
	
	public void deleteCrime(Crime c){
		mCrimes.remove(c);
	}
	
}
