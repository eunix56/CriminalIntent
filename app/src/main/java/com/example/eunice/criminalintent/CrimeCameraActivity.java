package com.example.eunice.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
    }
}
