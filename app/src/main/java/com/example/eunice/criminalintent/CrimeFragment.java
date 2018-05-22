package com.example.eunice.criminalintent;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeFragment extends Fragment {

    private static final String TAG = "CrimeFragment";
    public static final String EXTRA_CRIME_ID = "com.example.eunice.criminalintent.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final String DIALOG_IMAGE = "image";
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 2;
    private Crime mCrime;
    private Button mCurrentDateButton;
    private Button mCrimeDateButton;
    private Button mCrimeTimeButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    public CrimeFragment() {
        // Required empty public constructor
    }

    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);
    }


    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        CrimeLab.get(getActivity()).saveCrimes();
//    }

    private void updateDate(){
        mCurrentDateButton.setText(mCrime.getmDate().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.fragment_crime_save:
                CrimeLab.get(getActivity()).saveCrimes();
                return true;
            case R.id.fragment_crime_share:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showPhoto (){
        Photo p = mCrime.getmPhoto();
        BitmapDrawable b = null;
        if (p != null){
            String path = getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawings(getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                Photo p = mCrime.getmPhoto();
                try {
                File file = getActivity().getFileStreamPath(p.getmFilename());
                boolean deleted = file.delete();
                    if (deleted) {
                        Toast.makeText(getActivity(), " Image deleted", Toast.LENGTH_SHORT).show();
                    }}
                catch (Exception e){
                    Log.e(TAG, " Image not deleted", e);
                }
                if (p == null) return false;
                mPhotoView.setImageDrawable(null);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });
        PackageManager pm = getActivity().getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras()> 0);
        if(!hasACamera){
            mPhotoButton.setEnabled(false);
        }
        mPhotoView = (ImageView)v.findViewById(R.id.crime_photo_image_view);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = mCrime.getmPhoto();
                if (mPhotoView.getDrawable() == null)return;
                if (p == null) return;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
                ImageFragment.newInstance(path)
                        .show(fm, DIALOG_IMAGE);
            }
        });
        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                registerForContextMenu(mPhotoView);
                getActivity().openContextMenu(mPhotoView);
                return true;
            }
        });

        Toolbar mToolbar = (Toolbar)v.findViewById(R.id.toolbar_crime_fragment);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.inflateMenu(R.menu.fragment_crime);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.fragment_crime_share:
                        break;
                    default:
                        CrimeFragment.super.onOptionsItemSelected(item);

                }
                return true;
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if(NavUtils.getParentActivityName(getActivity()) != null){
                getActivity().setActionBar(mToolbar);
                if(getActivity().getActionBar() != null)
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);}
        }



        EditText mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCrimeDateButton = (Button)v.findViewById(R.id.date_of_crime);
        mCrimeDateButton.setText(mCrime.getDateString());
        mCrimeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment date_dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                date_dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                date_dialog.show(fm, DIALOG_DATE);
            }
        });
        mCrimeTimeButton = (Button)v.findViewById(R.id.time_of_crime);
        mCrimeTimeButton.setText(mCrime.getTimeString());
        mCrimeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment time_dialog = TimePickerFragment.newInstance(mCrime.getmDate());
                time_dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                time_dialog.show(fm, DIALOG_TIME);
            }
        });
        mCurrentDateButton = (Button)v.findViewById(R.id.crime_date);
        updateDate();
//        mCurrentDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                ChoiceFragment dialog = new ChoiceFragment();
//                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
//                dialog.show(fm, DIALOG_DATE);
//            }
//        });

        CheckBox mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        return v;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_DATE ){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            mCrimeDateButton.setText(mCrime.getDateString());
        }
        if(requestCode == REQUEST_TIME){
            Date mDate = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setmDate(mDate);
            mCrimeTimeButton.setText(mCrime.getTimeString());
        }else if(requestCode == REQUEST_PHOTO){
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null){
                Photo p = new Photo(filename);
                mCrime.setmPhoto(p);
                showPhoto();
            }
        }
    }



}
