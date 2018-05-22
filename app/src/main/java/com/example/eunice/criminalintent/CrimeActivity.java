package com.example.eunice.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.id.viewPager);

    }
}
