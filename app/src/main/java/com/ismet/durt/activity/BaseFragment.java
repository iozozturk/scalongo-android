package com.ismet.durt.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ismet.durt.sservices.DurtService;
import com.ismet.durt.utils.Stash;

import java.util.logging.Logger;

/**
 * Created by ismet on 04/02/16.
 */
public class BaseFragment extends Fragment {
    DurtService.Services ROUTES;
    public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    Stash stash;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ROUTES = new DurtService().ROUTES;
        stash = new Stash(this.getActivity());
    }

    public void redirectToFragment(BaseFragment baseFragment, Bundle bundle, boolean addToBackStack) {
        ((BaseActivity) getActivity()).redirectToFragment(baseFragment, bundle, addToBackStack);
    }

    public void redirectToActivity(Class<? extends BaseActivity> cls, Bundle bundle, boolean addToBackStack) {
        ((BaseActivity) getActivity()).redirectToActivity(cls, bundle, addToBackStack);
    }
}
