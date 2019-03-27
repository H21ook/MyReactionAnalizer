package com.example.myreactionanalizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.example.myreactionanalizer.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                useLoginInformation(accessToken);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("ERROR", exception.getMessage());
            }
        });
    }

    private void useLoginInformation(AccessToken accessToken) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
//
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                Bundle bundle = new Bundle();
//                try {
//                    String name = object.getString("name");
//                    String email = object.getString("email");
//                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
//                    bundle.putString("userName", name);
//                    bundle.putString("userEmail", email);
//                    bundle.putString("userImg", image);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtras(bundle);
//                intent.putExtra("jsondata", object.toString());
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id, name, email, picture.width(200)");
//        request.setParameters(parameters);
//        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
