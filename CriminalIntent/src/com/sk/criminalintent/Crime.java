package com.sk.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	
	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	
	
	public JSONObject toJSON() throws JSONException{
		
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		return json;
	}
	
	public Crime(JSONObject json) throws JSONException{
		mId = UUID.fromString(json.getString(JSON_ID));
		System.out.println("mId: "+mId);
		if(json.has(JSON_TITLE)){
			mTitle = json.getString(JSON_TITLE);
		}
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
	}
	
	
	
	public Crime(){
		mId = UUID.randomUUID();
		mDate = new Date();
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public UUID getmId() {
		return mId;
	}

	public Date getmDate() {
		return mDate;
	}

	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}

	public boolean ismSolved() {
		return mSolved;
	}

	public void setmSolved(boolean mSolved) {
		this.mSolved = mSolved;
	}
	
	@Override
	public String toString() {
		return mTitle;
	}
}
