package com.peterombodi.newconverterlab.presentation.screen.organisation_list.view;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.peterombodi.newconverterlab.data.geo.GeoCoderLoader;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.presenter.BankListPresenter;

import java.util.ArrayList;

/**
 * Created by Admin on 22.11.2016.
 */

public class BankListFragment extends Fragment implements IListFragment.IFragment, IListFragment.IView, SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<LatLng> {

    private static final String TAG = "BankListFragment";
    static final int LOADER_GEOCODER_ID = 1;

    private BankRVAdapter rvAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private View view;
    private IListFragment.IPresenter presenter;
    private IMainScreen.IRecyclerView iRecyclerView;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog progressDialog;

    public BankListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        context = getActivity();

        presenter = new BankListPresenter();
        presenter.registerView(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_banks_FL);

        setHasOptionsMenu(true);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer_FL);

        if (swipeContainer != null) {
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    presenter.refreshData();
                    //startDataLoadingService(Constants.TASK_REFRESH_DATA, "", null);

                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            //   swipeContainer.setEnabled(false);
        }
         return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_option, menu);
        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        iRecyclerView = (IMainScreen.IRecyclerView) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        iRecyclerView = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        presenter.unRegisterView();

        if (getLoaderManager().getLoader(LOADER_GEOCODER_ID) != null) {
            getLoaderManager().destroyLoader(LOADER_GEOCODER_ID);
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        //presenter.getBankList(null);
        presenter.refreshData();
        super.onResume();
    }

    @Override
    public void onRefreshRV(ArrayList<OrganizationRV> arrayList) {
        rvAdapter.animateTo(arrayList);
        recyclerView.scrollToPosition(0);
        swipeContainer.setRefreshing(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String _query) {
        presenter.getBankList(_query);
        return true;
    }


    @Override
    public void setRvArrayList(ArrayList<OrganizationRV> _rvArrayList) {
        if (recyclerView != null && recyclerView.getLayoutManager() != null && recyclerView.getAdapter() != null) {
            rvAdapter.animateTo(_rvArrayList);
            recyclerView.scrollToPosition(0);
        } else {
           if (recyclerView != null) {
                final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
            }
            rvAdapter = new BankRVAdapter(presenter, _rvArrayList);
//            rvAdapter = new BankRVAdapter(context, _rvArrayList);
            recyclerView.setAdapter(rvAdapter);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            recyclerView.setItemAnimator(itemAnimator);
        }
        if (swipeContainer != null) swipeContainer.setRefreshing(false);
    }

    @Override
    public void openDetail(OrganizationRV _organizationRV) {

    }

    @Override
    public void openLink(String _url) {
        Log.d(TAG, "openLink _url =" + _url);
        iRecyclerView.openLink(_url);
    }

    @Override
    public void openMap(String _region, String _city, String _address) {
//        iRecyclerView.openMap(_latLng);
        if (checkPlayServices()) {
            String strAddress = _region + "," + _city + "," + _address;
            Log.d(TAG, "openMap: " + strAddress);
            Bundle bundle = new Bundle();
            bundle.putString(GeoCoderLoader.ARGS_ADDRESS, strAddress);
            if (getLoaderManager().getLoader(LOADER_GEOCODER_ID) == null) {
                getLoaderManager().initLoader(LOADER_GEOCODER_ID, bundle, this);
            } else {
                getLoaderManager().restartLoader(LOADER_GEOCODER_ID, bundle, this);
            }
        }
    }

    @Override
    public void openCaller(String _phone) {
    }


    @Override
    public Loader<LatLng> onCreateLoader(int id, Bundle args) {
        GeoCoderLoader loader = null;
        if (id == LOADER_GEOCODER_ID) {
            loader = new GeoCoderLoader(context, args);
            Log.d(TAG, "onCreateLoader: " + loader.hashCode());
            loader.forceLoad();
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<LatLng> loader, LatLng _latLng) {
        if (_latLng != null) {
            iRecyclerView.openMap(_latLng);
        } else {
            Toast.makeText(context, getResources().getString(R.string.msg_no_latlng), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLoaderReset(Loader<LatLng> loader) {
        Log.d(TAG, "onLoaderReset: " + loader.hashCode());
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,9999).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void showProgress(int _itemNo, int _itemTotal) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.loading_data_msg));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(_itemTotal);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }
        progressDialog.setProgress(_itemNo);
        if (_itemNo==_itemTotal) progressDialog.cancel();

    }


}