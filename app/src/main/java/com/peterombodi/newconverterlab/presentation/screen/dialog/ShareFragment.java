package com.peterombodi.newconverterlab.presentation.screen.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.global.Constants;
import com.peterombodi.newconverterlab.presentation.Application;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.customView.CoursesView;
import com.peterombodi.newconverterlab.utils.BitmapCompress;

import java.io.File;
import java.util.ArrayList;


public class ShareFragment extends DialogFragment
        implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<File>{
    private static final String TAG = "ShareFragment";
    private static final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    private static final int LOADER_BITMAP_ID = 8;

    private CoursesView coursesView;

    public static ShareFragment newInstance(OrganizationRV _organization, ArrayList<Currency> _courseList) {
        ShareFragment f = new ShareFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ORGANIZATION, _organization);
        args.putParcelableArrayList(Constants.KEY_ARRAY_LIST, _courseList);
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container);
        OrganizationRV bank = getArguments().getParcelable(Constants.KEY_ORGANIZATION);
        ArrayList<Currency> courseList = getArguments().getParcelableArrayList(Constants.KEY_ARRAY_LIST);
        coursesView = (CoursesView) view.findViewById(R.id.cv_FS);
        view.findViewById(R.id.btn_share_FS).setOnClickListener(this);
        coursesView.setData(bank, courseList);
        return view;
    }

    @Override
    public void onStart() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int viewHeight = (int) (displaymetrics.heightPixels * 0.7);
        int viewWidth = (int) (displaymetrics.widthPixels * 0.95);
        // safety check
        if (getDialog() == null) return;
        LinearLayout layout = (LinearLayout) getDialog().findViewById(R.id.linearLayout_FS);
        if (layout!=null)   getDialog().getWindow().setLayout(viewWidth, viewHeight);
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS, REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            loadBitmap();
        }        
    }

    @Override
    public Loader<File> onCreateLoader(int id, Bundle args) {
        return new BitmapCompress(Application.getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<File> loader, File _data) {
        shareImage(_data);
    }

    @Override
    public void onLoaderReset(Loader<File> loader) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadBitmap();
                } else {
                    Snackbar.make(getView(), getString(R.string.msg_permission_storage_not_granted),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .show();
                }
            }
        }
    }


    private void loadBitmap(){
        Bitmap bitmap = coursesView.drawBitmap();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BitmapCompress.ARGS_BITMAP,bitmap);
        if (getLoaderManager().getLoader(LOADER_BITMAP_ID) == null) {
            getLoaderManager().initLoader(LOADER_BITMAP_ID, bundle, this);
        } else {
            getLoaderManager().restartLoader(LOADER_BITMAP_ID, bundle, this);
        }
    }
        
    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.msg_share_courser)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Application.getContext(), getResources().getString(R.string.msg_no_app_share), Toast.LENGTH_SHORT).show();
        }
        getDialog().dismiss();
    }

}
