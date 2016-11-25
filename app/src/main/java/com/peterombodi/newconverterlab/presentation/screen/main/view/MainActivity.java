package com.peterombodi.newconverterlab.presentation.screen.main.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.global.Constants;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.legalNotice.LegalNoticesActivity;
import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;
import com.peterombodi.newconverterlab.presentation.screen.main.presenter.MainScreenPresenter;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.view.BankListFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements IMainScreen.IView,IMainScreen.IRecyclerView,OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG";

    private IMainScreen.IPresenter presenter;

    private Fragment listFragment;
    private Fragment detailFragment;
    private View mLayout;
    private boolean firstRun = true;
    private LatLng bankLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.activity_main);

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
            commitListFragment(null);
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

    @Override
    public void openDetail(OrganizationRV _organizationRV) {
    }

    @Override
    public void openLink(String _url) {
        Log.d(TAG, "openLink _url =" + _url);
    }

    @Override
    public void openMap(LatLng _latLng) {
        if (_latLng != null) {
            bankLatLng = _latLng;
            Log.d(TAG, "bankLatLng = " + bankLatLng);
            selectAction(_latLng);
        }
    }

    @Override
    public void openCaller(String _phone) {
    }


    private void selectAction(final LatLng _latLng) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        // set dialog message
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle(getResources().getString(R.string.select_action4map));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.google_map),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        commitMapFragment(_latLng);
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
                        LatLngBounds latLngBounds = toBounds(_latLng, 100);
                        showPlace(latLngBounds);

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void commitMapFragment(LatLng latLng) {
        bankLatLng = latLng;
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(Constants.TAG_MAP_FRAGMENT)
                    .replace(R.id.fragment_container, mapFragment, MAP_FRAGMENT_TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
        mapFragment.getMapAsync(this);

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
    public void onMapReady(GoogleMap googleMap) {
        if (bankLatLng != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(bankLatLng)
                    .zoom(17)
                    .bearing(0)
                    .tilt(20)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(bankLatLng).title(""));
            googleMap.animateCamera(cameraUpdate);
        }
    }
}
