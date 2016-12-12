package com.peterombodi.newconverterlab.presentation;

import android.content.Context;

import com.peterombodi.newconverterlab.domain.DomainImpl;
import com.peterombodi.newconverterlab.domain.IDomain;
import com.peterombodi.newconverterlab.presentation.screen.organisationList.presenter.BankListPresenter;

/**
 * Created by Admin on 09.12.2016.
 */

public final class ObjectGraph {

    private static ObjectGraph graph;

    public static final ObjectGraph getInstance(final Context _context) {
        if (graph == null) {
            graph = new ObjectGraph(_context);
        }
        return graph;
    }

    private final IDomain mDomain;
    private final BankListPresenter presenter;

    public ObjectGraph(final Context _context) {
        mDomain = new DomainImpl();
        presenter = new BankListPresenter();
    }

    public final IDomain getDomainModule() {
        return mDomain;
    }

    public final BankListPresenter getPresenterModule() {
        return presenter;
    }



}