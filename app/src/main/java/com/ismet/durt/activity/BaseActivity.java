package com.ismet.durt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ismet.durt.R;
import com.ismet.durt.sservices.DurtService;
import com.ismet.durt.utils.Stash;

import java.util.logging.Logger;

/**
 * Created by ismet on 27/01/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    DurtService.Services ROUTES;
    public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    Stash stash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ROUTES = new DurtService().ROUTES;
        stash = new Stash(this);
    }

    // TODO: 03/02/16 Consider moving these methods to utility class for composition
    public void redirectToFragment(BaseFragment fragment, Bundle bundle, boolean addToBackStack) {

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        transaction.commit();
    }

    public void redirectToActivity(Class<? extends BaseActivity> cls, Bundle bundle, boolean addToBackStack) {

        Intent intent = new Intent(this, cls);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        startActivity(intent);

        if (!addToBackStack) {
            finish();
        }
    }
}
