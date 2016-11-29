package com.peterombodi.newconverterlab.presentation.screen.organisation_list.view;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import com.peterombodi.newconverterlab.data.api.GetDbData;
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

public class BankListFragment extends Fragment implements IListFragment.IFragment,
        IListFragment.IView,
        SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks {

    private static final String TAG = "BankListFragment";
    static final int LOADER_GEOCODER_ID = 1;
    static final int LOADER_DATABASE_ID = 2;
    private static final String SAVED_LAYOUT_MANAGER = "SAVED_LAYOUT_MANAGER";
    private static final String SEARCH_KEY = "SEARCH_KEY";


    private BankRVAdapter rvAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private View view;
    private IListFragment.IPresenter presenter;
    private IMainScreen.IRecyclerView iRecyclerView;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog progressDialog;
    private Parcelable layoutManagerSavedState;
    private SearchView searchView;
    private MenuItem item;
    private String searchQuery;
    private boolean firstRun;


    public BankListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_list, container, false);
        context = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_banks_FL);
        firstRun = (savedInstanceState==null);
        Log.d(TAG,">>>>>> firstRun = "+firstRun);

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
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark,
                    android.R.color.darker_gray,
                    android.R.color.holo_red_dark,
                    android.R.color.tertiary_text_dark);

            //   swipeContainer.setEnabled(false);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_option, menu);
        item = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);

        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        //searchView.setQuery();
        searchView.setOnQueryTextListener(this);

        if (searchQuery != null ) {
            if (!TextUtils.isEmpty(searchQuery)) item.expandActionView();
            searchView.setQuery(searchQuery, true);
            searchView.clearFocus();
        }

    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, ">>>>----- onAttach presenter isnull = "+(presenter==null));
        iRecyclerView = (IMainScreen.IRecyclerView) context;


        if (presenter==null) presenter = new BankListPresenter();
        presenter.registerView(this);
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        Log.d(TAG, ">>>>----- onResume ");
        getDbData(null);

        if (searchView != null && searchQuery != null)
            searchView.setQuery(searchView.getQuery(), true);

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, ">>>>----- onDestroyView");
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (getLoaderManager().getLoader(LOADER_GEOCODER_ID) != null) {
            getLoaderManager().destroyLoader(LOADER_GEOCODER_ID);
        }
        presenter.unRegisterView();
        iRecyclerView = null;
        super.onDestroyView();
    }

    @Override
    public void onRefreshRV(ArrayList<OrganizationRV> arrayList) {
        rvAdapter.animateTo(arrayList);
        recyclerView.scrollToPosition(0);
        swipeContainer.setRefreshing(false);
    }

    @Override
    public boolean onQueryTextSubmit(String _query) {
        Log.d(TAG, ">>>>>> onQueryTextSubmit query = " + _query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String _query) {
        Log.d(TAG, ">>>>----- onQueryTextChange query = '" + _query + "'/ searchQuery = '"+searchQuery+"'");
        if (!searchQuery.equals(_query)) getDbData(_query);
        searchQuery = _query;
        return true;
    }


    @Override
    public void setRvArrayList(ArrayList<OrganizationRV> _rvArrayList) {
        Log.d(TAG,">>>>----- setRvArrayList");
        if (recyclerView != null && recyclerView.getLayoutManager() != null && recyclerView.getAdapter() != null) {
            rvAdapter.animateTo(_rvArrayList);
            recyclerView.scrollToPosition(0);
        } else {
            rvAdapter = new BankRVAdapter(presenter, _rvArrayList);
            recyclerView.setAdapter(rvAdapter);
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
        iRecyclerView.openCaller(_phone);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_GEOCODER_ID:
                return new GeoCoderLoader(context, args);
            case LOADER_DATABASE_ID:
                return new GetDbData(context, args);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object _data) {
        switch (loader.getId()) {
            case LOADER_GEOCODER_ID:
                if (_data != null) {
                    iRecyclerView.openMap((LatLng) _data);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.msg_no_latlng), Toast.LENGTH_SHORT).show();
                }
                break;
            case LOADER_DATABASE_ID:
                if (_data != null) {
                    presenter.setRvArrayList((ArrayList<OrganizationRV>) _data);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.msg_no_latlng), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(TAG, "onLoaderReset: " + loader.hashCode());
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result, 9999).show();
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
        if (_itemNo == _itemTotal) progressDialog.cancel();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState recyclerView.getLayoutManager() isnull =" + (recyclerView.getLayoutManager() == null));
        super.onSaveInstanceState(outState);
        if (recyclerView.getLayoutManager() != null) {
            outState.putString(SEARCH_KEY, searchView.getQuery().toString());

        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewStateRestored");
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(SEARCH_KEY);
        }
        super.onViewStateRestored(savedInstanceState);


    }


    @Override
    public void getDbData(String _string) {
        Bundle bundle = new Bundle();
        bundle.putString(GetDbData.ARGS_FILTER, _string);
        if (getLoaderManager().getLoader(LOADER_DATABASE_ID) == null) {
            getLoaderManager().initLoader(LOADER_DATABASE_ID, bundle, this);
        } else {
            getLoaderManager().restartLoader(LOADER_DATABASE_ID, bundle, this);
        }
    }

}


