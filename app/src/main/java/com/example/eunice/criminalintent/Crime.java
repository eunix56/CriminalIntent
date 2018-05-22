package com.example.eunice.criminalintent;

import java.text.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;

/**
 * Created by Eunice on 3/15/2018.
 */

public class Crime {
    private static final String JSON_ID= "id";
    private static final String JSON_TITLE= "title";
    private static final String JSON_SOLVED= "solved";
    private static final String JSON_DATE= "date";
    private static final String JSON_PHOTO= "photo";

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private Photo mPhoto;


    public Crime(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(JSONObject json) throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE)){
        mTitle = json.getString(JSON_TITLE);}
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
        if(json.has(JSON_PHOTO)){
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }

    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_DATE, mDate.getTime());
        if(mPhoto != null){
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }
        return json;
    }

    public String toString(){
        return mTitle;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getDateString(){
        return DateFormat.getDateInstance(0).format(mDate);
    }
    public String getTimeString(){
        return DateFormat.getTimeInstance(1).format(mDate);
    }

    public Date getmDate() {
        return mDate;
    }


    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public Photo getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(Photo p) {
        this.mPhoto = p;
    }
}
