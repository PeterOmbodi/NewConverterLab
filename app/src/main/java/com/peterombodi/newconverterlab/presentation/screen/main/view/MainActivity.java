package com.peterombodi.newconverterlab.presentation.screen.main.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.global.Constants;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.legalNotice.LegalNoticesActivity;
import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;
import com.peterombodi.newconverterlab.presentation.screen.main.presenter.MainScreenPresenter;
import com.peterombodi.newconverterlab.presentation.screen.mapFragment.MapViewFragment;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.view.BankListFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements IMainScreen.IView, IMainScreen.IGetAction {

    private static final String TAG = "MainActivity";
    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG";
    private static final int REQUEST_PERMISSIONS_CALL = 0;
    // package for calling changed for API SDK>=21
    private static final String CALL_PACKAGE = (Build.VERSION.SDK_INT >= 21) ?
            "com.android.server.telecom" : "com.android.phone";
    private static final String[] PERMISSIONS_CALL = {Manifest.permission.CALL_PHONE};
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    private IMainScreen.IPresenter presenter;

    private Fragment listFragment;
    //private SupportMapFragment mapFragment;
    private Fragment mapFragment;
    private Fragment detailFragment;
    private View mLayout;
    private boolean firstRun = true;
    private LatLng bankLatLng;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.activity_main);
        if (savedInstanceState == null) {
            commitListFragment(null);
        }

    }

    @Override
    protected void onDestroy() {
        presenter.unRegisterView();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new MainScreenPresenter();
        presenter.registerView(this);
        if (firstRun) {

//            presenter.domainCallTest("call from view");
            //commitListFragment(null);
            firstRun = false;
        }
// TODO: 22.11.2016 pause for job?

    }


    @Override
    public void setBankListFragment(ArrayList<OrganizationRV> _rvArrayList) {
        //commitListFragment(_rvArrayList);
    }

    // show organizations list fragment
    private void commitListFragment(ArrayList<OrganizationRV> _arrayList) {
        if (listFragment == null) {
            // Create fragment and give it an argument for the selected article
            listFragment = new BankListFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.KEY_ARRAY_LIST, _arrayList);
            listFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, listFragment, Constants.TAG_BANK_LIST_FRAGMENT)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    private void commitMapFragment(Address _address) {
        mapFragment = new MapViewFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_ADDRESS, _address);
        mapFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(MAP_FRAGMENT_TAG)
                .replace(R.id.fragment_container, mapFragment, MAP_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    @Override
    public void openDetail(OrganizationRV _organizationRV) {
    }

    @Override
    public void openLink(String _url) {

        Log.d(TAG, "openLink _url =" + _url);
        if (!_url.startsWith(HTTP) && !_url.startsWith(HTTPS)) _url = HTTP + _url;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void openMap(Address _address) {
        Log.d(TAG, "openMap _address =" + _address.toString());
        if (_address != null) {
//            bankLatLng = _latLng;
//            Log.d(TAG, "bankLatLng = " + bankLatLng);
            selectAction(_address);
        }
    }

    @Override
    public void openCaller(String _phone) {
        phoneCall(_phone);
    }

    private void selectAction(final Address _address) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        // set dialog message
        Log.d(TAG, "alertDialogBuilder set dialog message");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle(getResources().getString(R.string.select_action4map));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.google_map),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        commitMapFragment(_address);
                    }
                });
        alertDialogBuilder.setNeutralButton(getResources().getString(R.string.legal),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showLegal();

                    }
                });
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.google_place),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LatLngBounds latLngBounds = toBounds(
                                new LatLng(_address.getLatitude(), _address.getLongitude()), 100);
                        showPlace(latLngBounds);
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


    private void showLegal() {
        startActivity(new Intent(this, LegalNoticesActivity.class));
    }

    private LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    private void showPlace(LatLngBounds latLngBounds) {
        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            intentBuilder.setLatLngBounds(latLngBounds);
            Intent intent = intentBuilder.build(this);
            // Start the Intent by requesting a result, identified by a request code.
            startActivityForResult(intent, 33);

        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    phoneCall(phoneNumber);
                } else {
                    Snackbar.make(mLayout, getString(R.string.msg_permission_not_granted),
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

    //check permission and make call
    private void phoneCall(String _phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            phoneNumber = _phoneNumber;
            ActivityCompat.requestPermissions(this, PERMISSIONS_CALL, REQUEST_PERMISSIONS_CALL);
            return;
        }
        try {
            // Call permissions have been granted. Calling.
            Uri number = Uri.parse("tel:" + _phoneNumber);
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(number);
            callIntent.setPackage(CALL_PACKAGE);
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException e) {
            Log.e(TAG, "ActivityNotFoundException " + e.toString());
        }
    }

}
