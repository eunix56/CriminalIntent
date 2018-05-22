package com.example.eunice.criminalintent;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Eunice on 3/19/2018.
 */

public class CrimeListFragment extends ListFragment{
    private static final int REQUEST_CRIME = 1;
    private static CrimeAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSubtitleVisible = false;
        getActivity().setTitle(R.string.crimes_title);
        ArrayList<Crime> mCrimes = CrimeLab.get(getActivity()).getmCrimes();
        adapter = new CrimeAdapter(getActivity(), 0, mCrimes);
        setListAdapter(adapter);
        setHasOptionsMenu(true);
        setRetainInstance(true);


    }

    public CrimeListFragment(){

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        toolbar = (Toolbar)v.findViewById(R.id.eunice_toolbar);
        toolbar.setTitle(R.string.app_name);
        getActivity().setActionBar(toolbar);
        floatingActionButton = (FloatingActionButton)v.findViewById(R.id.floating_action_new_crime);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, 0);

            }
        });
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            registerForContextMenu(listView);
        }else{
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                    menuInflater.inflate(R.menu.crime_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
                            CrimeLab crimeLab = CrimeLab.get(getActivity());
                            for (int i = adapter.getCount()-1; i>=0; i--){
                                if(getListView().isItemChecked(i)) {
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }


                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem item = menu.findItem(R.id.menu_item_subtitle);
        if(mSubtitleVisible && item != null){
            item.setTitle(R.string.hide_subtitle);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_search:
                break;
            case R.id.menu_item_share:
                break;
            case R.id.menu_item_subtitle:
                getActivity().setActionBar(toolbar);
                if (getActivity().getActionBar() != null)
                    if(getActivity().getActionBar().getSubtitle() == null){
                        getActivity().getActionBar().setSubtitle(R.string.subtitle);
                        mSubtitleVisible = true;
                        item.setTitle(R.string.hide_subtitle);
                    }
                    else{
                        getActivity().getActionBar().setSubtitle(null);
                        mSubtitleVisible = false;
                        item.setTitle(R.string.show_subtitle);
                    }
                break;
            case R.id.menu_item_load:
                CrimeLab.get(getActivity()).loadCrimes();
                break;
            case R.id.menu_item_settings:
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        CrimeAdapter crimeAdapter = (CrimeAdapter)getListAdapter();
        Crime crime = crimeAdapter.getItem(position);

        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                crimeAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c= ((CrimeAdapter)getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        assert c != null;
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivity(i);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CRIME){

        }
    }


    private class CrimeAdapter extends ArrayAdapter<Crime>{

        CrimeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Crime> objects) {
            super(getActivity(), 0 , objects);
        }



        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }
            Crime c = getItem(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c != null ? c.getmTitle() : null);
            TextView dateTextView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            assert c != null;
            dateTextView.setText(c.getDateString());
            TextView timeTextView = (TextView)convertView.findViewById(R.id.crime_list_item_timeTextView);
            timeTextView.setText(c.getTimeString());
            CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.ismSolved());
            return convertView;
        }
    }
}





