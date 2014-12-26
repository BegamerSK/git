package com.sk.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class CrimeLab {
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	
	public void addCrime(Crime c){
		mCrimes.add(c);
	}
	
	public Context getmAppContext() {
		return mAppContext;
	}

	private ArrayList<Crime> mCrimes;
	
	public ArrayList<Crime> getmCrimes() {
		return mCrimes;
	}

	private CrimeLab(Context appContext){
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
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
	
}
