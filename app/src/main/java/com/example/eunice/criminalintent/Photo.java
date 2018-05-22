package com.example.eunice.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Eunice on 4/12/2018.
 */

public class Photo {
    private static final String JSON_FILENAME = "filename";
    private String mFilename;


    public String getmFilename() {
        return mFilename;
    }


    public Photo(String filename){
        mFilename = filename;
    }

    public Photo(JSONObject json) throws JSONException{
        mFilename = json.getString(JSON_FILENAME);
    }
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

}
