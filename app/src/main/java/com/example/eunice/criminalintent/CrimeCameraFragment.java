package com.example.eunice.criminalintent;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeCameraFragment extends Fragment {
    private static final String TAG = "CrimeCameraFragment";
    public static final String EXTRA_PHOTO_FILENAME = "com.example.eunice.criminalintent.photo_filename";
    private Camera camera;
    private SurfaceView mSurfaceView;
    private View mProgressBar;
    private boolean safeToTakePicture = false;
    String filename;
    File file;
    File mFile;

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    };


    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            filename = UUID.randomUUID().toString() + ".jpg";
            FileOutputStream os = null;
            boolean success = true;
            safeToTakePicture = true;
            file = getActivity().getFilesDir();
            mFile = new File(file, filename);
            try {
                os = new FileOutputStream(mFile, true);
                os.write(data);
            }catch (Exception e){
                Log.e(TAG, "Error writing to file", e);
                success = false;
            }finally {
                try {
                    if (os != null){
                        os.close();
                    }
                }catch (Exception e){
                    Log.e(TAG, "Error closing file", e);
                    success = false;
                }
            }
            if (success){
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME, filename);
                getActivity().setResult(Activity.RESULT_OK, i);
            }else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
            safeToTakePicture = true;
        }
    };

//    public void deleteImage(String mPath){
//        if (mPath != null){
//            boolean deleted = getActivity().deleteFile(mPath);
//            if (deleted){
//                Toast.makeText(getActivity(), " Image deleted", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public CrimeCameraFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            camera = Camera.open(0);
        }else {
            camera = Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (camera != null){
            camera.release();
            camera = null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime_camera, container, false);
        mProgressBar = v.findViewById(R.id.crime_camera_progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_take_picture_button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (safeToTakePicture){
                if(camera != null){
                    camera.takePicture(mShutterCallback, null, mJpegCallback);
                    safeToTakePicture = false;
                }}
            }
        });
        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surface_view);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(camera != null){
                        camera.setPreviewDisplay(holder);
                    }
                }catch (IOException exception){
                    Log.e(TAG, "Error setting up preview display", exception);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (camera == null) return;
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(s.width, s.height);
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
                parameters.setPictureSize(s.width, s.height);
                camera.setParameters(parameters);
                try {
                    camera.startPreview();
                    safeToTakePicture = true;
                }catch (Exception e){
                    Log.e(TAG, "Could not start preview", e);
                    camera.release();
                    camera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera!= null){
                    camera.stopPreview();
                }
            }
        });
        return v;
    }

    @SuppressWarnings("deprecation")
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height){
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size s: sizes){
            int area = s.width * s.height;
            if(area > largestArea){
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

}
