package com.example.eunice.criminalintent;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import com.example.eunice.criminalintent.CrimeLab;

import org.json.JSONException;

/**
     * Created by Eunice on 3/19/2018.
     */
    public class CrimeLab {
        private static final String TAG= "CrimeLab";
        private static final String FILENAME= "crimes.json";

        private ArrayList<Crime> mCrimes;
        private CriminalIntentJSONSerializer mSerializer;
        private static CrimeLab sCrimeLab;
        private Context mAppContext;

        private CrimeLab(Context appContext){
            mAppContext = appContext;
            mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
            loadCrimes();}

        public boolean loadCrimes(){
        try {
            mCrimes = mSerializer.loadCrimes();
            return true;
        }catch (Exception e){
            mCrimes = new ArrayList<>();
            Log.e(TAG, "Error loading file: " ,e);
            return false;
        }
    }

        public void addCrime(Crime crime){
            mCrimes.add(crime);
        }

        public  boolean saveCrimes(){
            try{
                mSerializer.saveCrimes(mCrimes);
                Log.d(TAG, "crimes saved to file");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Error saving crimes" + e);
                return false;
            }
        }

        public void deleteCrime(Crime c){
            mCrimes.remove(c);
        }
        public static CrimeLab get(Context c){
            if (sCrimeLab == null){
                sCrimeLab = new CrimeLab(c.getApplicationContext());
            }
            return sCrimeLab;
        }


        public ArrayList<Crime> getmCrimes() {
            return mCrimes;
        }

        public Crime getCrime(UUID Id){
            for(Crime c: mCrimes){
                if(c.getId().equals(Id)){
                    return c;
                }
            }
            return null;
        }
    }


