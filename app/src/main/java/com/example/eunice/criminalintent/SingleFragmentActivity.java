package com.example.eunice.criminalintent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import static android.R.id.list;

/**
 * Created by Eunice on 3/19/2018.
 */

public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if(fragment== null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }

//    public void setFloatingActionButton(){
//        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floating_action_new_crime);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Crime crime = new Crime();
//                CrimeLab.get(getApplication()).addCrime(crime);
//                Intent i = new Intent(getApplication(), CrimePagerActivity.class);
//                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
//                startActivityForResult(i, 0);
//
//            }
//        });
//    }

}
