package com.peterombodi.newconverterlab.presentation.screen.main.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.global.Constants;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IRecyclerView;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.view.BankListFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements IRecyclerView,IMainScreen.IView {

    private static final String TAG = "MainActivity";


    private IMainScreen.IPresenter presenter;

    private Fragment listFragment;
    private Fragment detailFragment;
    private View mLayout;
    private boolean firstRun = true;


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
        if (firstRun) {
//            presenter = new MainScreenPresenter();
//            presenter.registerView(this);
//            presenter.domainCallTest("call from view");
            commitListFragment(null);
            firstRun = false;
        }
// TODO: 22.11.2016 pause for job?

    }

    @Override
    public void setText(String _text) {

    }

    @Override
    public void setBankListFragment(ArrayList<OrganizationRV> _rvArrayList) {

        commitListFragment(_rvArrayList);
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
    public void openDetail(String id, String name, String region, String city, String address, String phone, String link, String date, String dateDelta) {

    }

    @Override
    public void openLink(String url) {

    }

    @Override
    public void openMap(String region, String city, String address) {

    }

    @Override
    public void openCaller(String phone) {

    }
}
