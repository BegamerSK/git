package com.sk.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
	
	private static final String JSON_FILENAME = "filename";
	
	private String filename;

	public Photo(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}
	
	public Photo(JSONObject json) throws JSONException{
		this.filename = json.getString(JSON_FILENAME);
	}
	
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_FILENAME, this.filename);
		return json;
	}

	public Photo() {
		super();
	}
	

}
