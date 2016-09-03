package com.example.prjagannath.castus.src;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.prjagannath.castus.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_videos", "user_friends"));
        loginButton.clearPermissions();
        loginButton.setPublishPermissions(Arrays.asList("publish_actions"));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                loginButton.setReadPermissions(Arrays.asList("user_videos"));
                Log.d("Token", FirebaseInstanceId.getInstance().getToken());

         }
        });


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mIntent = new Intent(LoginActivity.this, PostLoginActivity.class);
                mIntent.putExtra("userID", loginResult.getAccessToken().getUserId());
                mIntent.putExtra("token", loginResult.getAccessToken().getToken());
                Log.d("Facebook ID",loginResult.getAccessToken().getUserId());
                Log.d("Facebook Token",loginResult.getAccessToken().getToken());
                final String[] friendList = new String[1];
                GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                        loginResult.getAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    friendList[0] = response.toString();
                                    Log.d("Your Mom!",friendList[0]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).executeAsync();
                mIntent.putExtra("friends", friendList[0]);
//                new GraphRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        "/{friend-list-id}",
//                        null,
//                        HttpMethod.GET,
//                        new GraphRequest.Callback() {
//                            public void onCompleted(GraphResponse response) {
//                                System.out.println(response.getJSONObject().toString());
//                            }
//                        }
//                ).executeAsync();

                startActivity(mIntent);
//                info.setText(
//                        "User ID: "
//                                + loginResult.getAccessToken().getUserId()
//                                + "\n" +
//                                "Auth Token: "
//                                + loginResult.getAccessToken().getToken()
//                );
            }

            @Override
            public void onCancel() {
                //Do nothing
            }

            @Override
            public void onError(FacebookException e) {
//                info.setText("Login attempt failed.");
                AlertDialog.Builder alertBox = new AlertDialog.Builder(getApplicationContext());
                alertBox.setTitle("Login attempt failed");
                alertBox.setMessage("Please try again!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertBox.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



}

