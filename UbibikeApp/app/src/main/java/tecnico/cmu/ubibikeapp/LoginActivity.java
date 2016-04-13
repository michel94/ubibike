package tecnico.cmu.ubibikeapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
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

        Button mUserSignInButton = (Button) findViewById(R.id.username_sign_in_button);
        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mUserRegisterButton = (Button) findViewById(R.id.user_register_button);
        mUserRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean validateForm(){
        if (mAuthTask != null) {
            return true;
        }

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUserView.setError(getString(R.string.error_invalid_username));
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }

        return cancel;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Log.d("login", "trying to login");
        if (validateForm())
            return;

        // Store values at the time of the login attempt.
        final String username = mUserView.getText().toString();
        final String password = mPasswordView.getText().toString();

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
        API api = new API();
        final Activity self = this;

        api.login(username, password, new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                try {
                    if(response.getBoolean("success")) {
                        Log.d("login", "correct credentials");

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("preferences", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.commit();

                        Intent intent = new Intent(self, MainActivity.class);
                        startActivity(intent);
                    }else {
                        Log.d("login", "incorrect credentials");
                    }
                } catch (JSONException e) {
                    Log.d("Login", "Invalid response format: " + response);
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Exception e) {
                //e.printStackTrace();
                Log.d("login", "I got you!");
            }
        });

    }

    private void attemptRegister() {
        Log.d("login", "trying to register");
        if (validateForm())
            return;

        // Store values at the time of the login attempt.
        String username = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        API api = new API();
        api.register(username, password, new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                try {
                    if(response.getBoolean("success"))
                        Log.d("login", "account created");
                    else
                        Log.d("login", "username already exists");
                } catch (JSONException e) {
                    Log.d("Login", "Invalid response format: " + response);
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Exception e) {
                //e.printStackTrace();
                Log.d("login", "I got you!");
            }
        });
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() >= 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

