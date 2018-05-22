package com.example.eunice.criminalintent;


import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH = "com.example.eunice.criminalintent.image_path";
    public ImageView imageView;
    public static Toolbar toolbar;


    public ImageFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_image, null);
        toolbar = (Toolbar)v.findViewById(R.id.toolbar_fragment);
        toolbar.setTitle("Image of Crime");
        imageView = (ImageView)v.findViewById(R.id.image_view_fragment);
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image = PictureUtils.getScaledDrawings(getActivity(), path);
        imageView.setImageDrawable(image);
        AlertDialog ser = new AlertDialog.Builder(getActivity()).setView(v).create();
        return ser;
    }



//    @Override
//    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        imageView = (ImageView)
//        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
//        BitmapDrawable image = PictureUtils.getScaledDrawings(getActivity(), path);
//        imageView.setImageDrawable(image);
//        return imageView;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (imageView != null)
        PictureUtils.cleanImageView(imageView);
    }

    public static ImageFragment newInstance(String imagePath){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePath);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return fragment;

    }

}
