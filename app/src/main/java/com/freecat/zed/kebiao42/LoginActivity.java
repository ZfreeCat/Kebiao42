package com.freecat.zed.kebiao42;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A login screen that offers login via id/password.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private final String actionA = "course";
    private final String actionB = "grade";

    private UserLoginTask mAuthTask = null;
    private String filename, action;

    // UI references.
    private EditText mIdView;
    private EditText mPasswordView;
    private View mProgressView;
    private LinearLayout linearLayout;
    private TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        filename = intent.getStringExtra("filename");
        action = intent.getStringExtra("action");


        Button mButtonS = (Button) findViewById(R.id.al_start);//start button
        mButtonS.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set up the login form.

                linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.bglayout_login, null);
                mIdView = (EditText) linearLayout.findViewById(R.id.id);
                textInputLayout = (TextInputLayout) linearLayout.findViewById(R.id.pwdWrapper);
                switch (action) {
                    case actionA:
                        textInputLayout.setHint(getString(R.string.prompt_password));
                        break;
                    case actionB:
                        textInputLayout.setHint(getString(R.string.prompt_idNum));
                        break;
                }
                mPasswordView = (EditText) linearLayout.findViewById(R.id.password);
                mProgressView = linearLayout.findViewById(R.id.login_progress);


                //set up an alertDialog to login
                new AlertDialog.Builder(LoginActivity.this).setTitle("登录").setView(linearLayout).setPositiveButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        attemptLogin();
                        //this try-catch intents to keep the alertDialog open
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            //set mShowing 2 fool android
                            field.set(dialog, false);  //set mShowing true when u want it closed
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            //set mShowing 2 fool android
                            field.set(dialog, true);  //set mShowing true when u want it closed
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
            }
        });


        Button mButtonQ = (Button) findViewById(R.id.al_quit);//quit button
        mButtonQ.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LoginActivity.this).setTitle("确定退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid id, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mIdView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String id = mIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a empty password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        //check for a valid id, if the user entered one.
        if (id.length() != 9) {
            mIdView.setError(getString(R.string.error_incorrect_format_id));
            focusView = mIdView;
            cancel = true;
        }

        // Check for a empty id.
        if (TextUtils.isEmpty(id)) {
            mIdView.setError(getString(R.string.error_field_required));
            focusView = mIdView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (!isNetworkConnected()) {
                Toast.makeText(Mapplication.getContext(), getString(R.string.bad_network), Toast.LENGTH_SHORT).show();
            } else {
                mProgressView.setVisibility(View.VISIBLE);
                mAuthTask = new UserLoginTask(id, password);
                mAuthTask.execute((Void) null);
            }
        }
    }


    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mId;
        private final String mPassword;

        UserLoginTask(String id, String password) {
            mId = id;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            return jsonGetter(mId, mPassword, action);
        }

        @Override
        protected void onPostExecute(final String data) {
            mAuthTask = null;
            mProgressView.setVisibility(View.GONE);

            //to analyse the data that fetched
            //according to the python that i wrote
            //if not gets a valid student account, it will return "bad information"
            if (data.equals("bad information")) {
                mPasswordView.setError(getString(R.string.error_incorrect_idORpwd));
                mPasswordView.requestFocus();
            } else {
                saveJson(filename, data);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mProgressView.setVisibility(View.GONE);
        }

    }

    public String jsonGetter(String mId, String mPassword, String mAction) {
        StringBuilder response;
        String mUrl = null;
        switch (mAction) {
            case (actionA):
                mUrl = "http://1.freecat0706.applinzi.com/?";
                break;
            case (actionB):
                mUrl = "http://1.freecat0706.applinzi.com/grade?";
                break;
        }
        //this url ref to a python which uses to post json data of a valid student account on my SAE
        String s_url = mUrl + "id=" + mId + "&pwd=" + mPassword;
        HttpURLConnection connection = null;

        //use httpURLConnection to fetch data
        try {
            URL url = new URL(s_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(in));
            response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        //this is the fetched data in string form
        return response.toString();
    }

    /**
     * check for network
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }


    /**
     * get the type of the current network(i have not used it yet)
     *
     * @return 0：no network   1：WIFI   2：WAP    3：NET
     */
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * use openFileOutput to save file
     *
     * @param filename
     * @param file
     */
    public void saveJson(String filename, String file) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

