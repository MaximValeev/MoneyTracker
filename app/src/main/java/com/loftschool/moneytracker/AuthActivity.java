package com.loftschool.moneytracker;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loftschool.moneytracker.api.Api;

import java.io.IOException;

public class AuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 999;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        Button buttonSignIn = findViewById(R.id.login_button);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess() && result.getSignInAccount() != null){
                final GoogleSignInAccount account = result.getSignInAccount();
                getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<AuthResult>() {
                    @Override
                    public Loader<AuthResult> onCreateLoader(int id, Bundle args) {
                        return new AsyncTaskLoader<AuthResult>(AuthActivity.this){
                            @Override
                            public AuthResult loadInBackground() {
                                Api api = ((App)getApplication()).getApi();
                                try {
                                    return api.auth(account.getId()).execute().body();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };
                    }

                    @Override
                    public void onLoadFinished(Loader<AuthResult> loader, AuthResult data) {
                        if(result != null && result.isSuccess()){
                            ((App)getApplication()).setAuthToken(result.getSignInAccount().getIdToken());
                            finish();
                        } else showError();
                    }

                    @Override
                    public void onLoaderReset(Loader<AuthResult> loader) {

                    }
                }).forceLoad();
            }
        }
    }

    private void showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }
}
