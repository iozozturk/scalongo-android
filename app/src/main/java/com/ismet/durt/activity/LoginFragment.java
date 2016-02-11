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
import com.ismet.durt.mmodels.responses.LoginResponse;
import com.ismet.durt.utils.CredentialUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends BaseFragment {

    private boolean isLoginRequestAlive = false;

    // @formatter:off
    @Bind(R.id.input_email) EditText mEmailView;
    @Bind(R.id.input_password) EditText mPasswordView;
    @Bind(R.id.btn_login) Button mEmailSignInButton;
    // @formatter:on
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(this.getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        return view;
    }

    private void attemptLogin() {
        if (isLoginRequestAlive) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

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

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            login(email, password);
        }
    }

    private void showProgress(final boolean show) {
        if (show)
            progressDialog.show();
        else
            progressDialog.hide();
    }


    @DebugLog
    public void login(String email, String password) {

        ROUTES.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response) {
                isLoginRequestAlive = false;
                showProgress(false);
                if (response.isSuccess()) {
                    logger.info("Login successful");
                    stash.setSessionId(response.body().getSessionId());
                } else {
                    logger.info("Login failed");
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }

            @DebugLog
            @Override
            public void onFailure(Throwable t) {
                isLoginRequestAlive = false;
                showProgress(false);
            }
        });
    }

    @OnClick(R.id.link_signup)
    public void redirectToSignup(){
        redirectToFragment(new SignupFragment(), new Bundle(), true);
    }
}