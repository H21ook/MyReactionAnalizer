package com.example.myreactionanalizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String USER_AGE_RANGE = "user_age_range";
    private static final String USER_LINK = "user_link";
    private static final String USER_PHOTOS = "user_photos";
    private static final String USER_POSTS = "user_posts";
    private static final String USER_FRIENDS = "user_friends";
    private static final String USER_HOMETOWN = "user_hometown";
    private static final String USER_LIKES = "user_hometown";
    private static final String USER_GENDER = "user_gender";
    private static final String USER_STATUS = "user_status";
    private static final String USER_VIDEOS = "user_videos";

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE, USER_AGE_RANGE, USER_LINK,
                USER_PHOTOS,USER_FRIENDS, USER_POSTS, USER_HOMETOWN, USER_LIKES, USER_GENDER, USER_STATUS, USER_VIDEOS));
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void nextActivity(Profile profile){
        if(profile != null){
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            main.putExtra("userFirstName", profile.getFirstName());
            main.putExtra("userLastName", profile.getLastName());
            main.putExtra("userID", profile.getId());
            main.putExtra("userImg", profile.getProfilePictureUri(200,200).toString());
            startActivity(main);
        }
    }
}
