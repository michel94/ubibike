package tecnico.cmu.ubibikeapp;

import android.app.Activity;
import android.content.Intent;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;


public class LoginActivity extends AppCompatActivity{

    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;

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

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Log.d("login", "trying to login");
        if (handleForm())
            return;

        final String username = mUserView.getText().toString();
        final String password = mPasswordView.getText().toString();

        API api = new API();
        final Activity self = this;

        api.login(username, password, new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                try {
                    if(response.getBoolean("success")) {
                        Log.d("login", "correct credentials");

                        Utils.setUsername(getApplicationContext(), username);
                        Utils.setPassword(getApplicationContext(), password);
                        Utils.setUserID(getApplicationContext(), (String) response.get("userID"));


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
                Log.d("login", "I got you!");
            }
        });

    }

    private void attemptRegister() {
        Log.d("login", "trying to register");
        if (handleForm())
            return;

        String username = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

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
                Log.d("login", "I got you!");
            }
        });
    }

    private boolean handleForm(){

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

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

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() >= 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

}

