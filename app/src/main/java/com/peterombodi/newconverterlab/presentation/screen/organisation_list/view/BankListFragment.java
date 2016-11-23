package com.peterombodi.newconverterlab.presentation.screen.organisation_list.view;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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

import com.peterombodi.newconverterlab.data.api.DownloadData;
import com.peterombodi.newconverterlab.data.api.DownloadDataImpl;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.global.Constants;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.presenter.BankRVAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 22.11.2016.
 */

public class BankListFragment extends Fragment implements IListFragment.IFragment,IListFragment.IView,SearchView.OnQueryTextListener {

    private static final String TAG = "BankListFragment";
    private ArrayList<OrganizationRV> bankArrayList;
    private ArrayList<OrganizationRV> bankArrayList4Filter;
    private BankRVAdapter rvAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private View view;
//    private IActivity iActivity;

    public BankListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        context = getActivity();
        if (getArguments() != null) {
            bankArrayList = getArguments().getParcelableArrayList(Constants.KEY_ARRAY_LIST);
            if (bankArrayList==null) {
                // TODO: 23.11.2016 NEW!!!
                Log.d(TAG,">>>>--- onCreateView.downloadData");
                DownloadData downloadData = new DownloadDataImpl();
                bankArrayList = downloadData.getDbData();
                Log.d(TAG,">>>>--- onCreateView. data downloaded!");
            }
            bankArrayList4Filter = new ArrayList<>();
            bankArrayList4Filter.addAll(bankArrayList);
            initializeRView();
        }
        setHasOptionsMenu(true);
        // TODO: 22.11.2016
//        iActivity = (IActivity) getActivity();
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

    // initialize RecyclerView and set adapter
    private void initializeRView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_banks_FL);
        if (recyclerView != null) {
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }

        rvAdapter = new BankRVAdapter(context, bankArrayList);
        recyclerView.setAdapter(rvAdapter);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
    }



    @Override
    public void onStop() {
        super.onStop();
        // TODO: 22.11.2016
//        iActivity.setPullDownRefreshEnabled(Constants.TAG_BANK_LIST_FRAGMENT, false);
    }


    @Override
    public void onResume() {
        // TODO: 22.11.2016
//        iActivity.setPullDownRefreshEnabled(Constants.TAG_BANK_LIST_FRAGMENT, true);
        super.onResume();
    }

    @Override
    public void onRefreshRV(ArrayList<OrganizationRV> arrayList) {
        rvAdapter.animateTo(arrayList);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<OrganizationRV> filteredModelList = filter(bankArrayList4Filter, query);
        rvAdapter.animateTo(filteredModelList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    private List<OrganizationRV> filter(List<OrganizationRV> models, String query) {
        query = query.toLowerCase();

        final List<OrganizationRV> filteredModelList = new ArrayList<>();
        for (OrganizationRV model : models) {
            final String title = model.getTitle().toLowerCase();
            final String region = model.getRegion().toLowerCase();
            final String city = model.getCity().toLowerCase();
            if (title.contains(query) || region.contains(query) || city.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void setRvArrayList(ArrayList<OrganizationRV> rvArrayList) {

    }
}