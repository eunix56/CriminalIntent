package com.example.eunice.criminalintent;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;
import java.util.UUID;


public class CrimePagerActivity extends FragmentActivity {

        private ArrayList<Crime> mCrimes;
        private ViewPager mViewPager;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mViewPager = new ViewPager(this);
            mViewPager.setId(R.id.viewPager);
            setContentView(mViewPager);


            mCrimes = CrimeLab.get(this).getmCrimes();
            FragmentManager fm = getSupportFragmentManager();
            mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
                @Override
                public Fragment getItem(int position) {
                    Crime crime = mCrimes.get(position);
                    return CrimeFragment.newInstance(crime.getId());
                }

                @Override
                public int getCount() {
                    return mCrimes.size();
                }
            });

            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    Crime crime = mCrimes.get(position);
                    if(crime.getmTitle() != null){
                        setTitle(crime.getmTitle());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
            for (int i=0;i < mCrimes.size(); i++){
                if(mCrimes.get(i).getId().equals(crimeId)){
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }

        }





    }


