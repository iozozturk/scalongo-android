package com.ismet.durt.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ismet.durt.R;
import com.ismet.durt.models.responses.GenericResponse;
import com.ismet.durt.utils.CredentialUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupFragment extends BaseFragment {

    private boolean isSignupRequestAlive = false;

    ProgressDialog progressDialog;
    // @formatter:off
    @Bind(R.id.input_name) EditText mNameView;
    @Bind(R.id.input_email) EditText mEmailView;
    @Bind(R.id.input_password) EditText mPasswordView;
    @Bind(R.id.btn_signup) Button mSignupButton;
    // @formatter:on

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(this.getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating new account...");

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptSignup();
                    return true;
                }
                return false;
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });

        return view;
    }

    private void attemptSignup() {
        if (isSignupRequestAlive) return;

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !CredentialUtils.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!CredentialUtils.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            signup(name, email, password);
        }
    }

    @DebugLog
    private void showProgress(final boolean show) {
        if (show) progressDialog.show();
        else progressDialog.hide();
    }

    @OnClick(R.id.link_login)
    public void redirectToLogin() {
        redirectToFragment(new LoginFragment(), new Bundle(), true);
    }


    @DebugLog
    public void signup(String name, String email, String password) {

        ROUTES.signup(name, "username_holder", email, password).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Response response) {
                isSignupRequestAlive = false;
                showProgress(false);
                if (response.isSuccess()) {
                    logger.info("Signup successful");
                } else {
                    logger.info("Signup failed");
                    logger.warning(response.raw().message());
                    mEmailView.setError(getString(R.string.error_existing_email));
                    mEmailView.requestFocus();
                }
            }

            @DebugLog
            @Override
            public void onFailure(Throwable t) {
                isSignupRequestAlive = false;
                showProgress(false);
            }
        });
    }

}
