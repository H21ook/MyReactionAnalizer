package com.example.myreactionanalizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<JSONObject> listContent = new ArrayList<>();
    ArrayList<JSONObject> reactions = new ArrayList<>();
    CustomAdapter postCustomAdapter = new CustomAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        TextView userName = (TextView) navHeaderView.findViewById(R.id.userName);
        CircleImageView userImg = (CircleImageView) navHeaderView.findViewById((R.id.userImg));

        Bundle bundle = getIntent().getExtras();

        String firstName = bundle.getString("userFirstName");
        String lastName = bundle.getString("userLastName");
        userName.setText(firstName + " " + lastName);
        Picasso.get()
                .load(bundle.getString("userImg"))
                .into(userImg);

        //Friends data
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            JSONArray jArrayLike = object.getJSONObject("like").getJSONArray("data");
                            JSONArray jArrayLove = object.getJSONObject("love").getJSONArray("data");
                            JSONArray jArrayHaha = object.getJSONObject("haha").getJSONArray("data");
                            JSONArray jArraySad = object.getJSONObject("sad").getJSONArray("data");
                            JSONArray jArrayAngry = object.getJSONObject("angry").getJSONArray("data");
                            JSONArray jArrayWow = object.getJSONObject("wow").getJSONArray("data");

                            if (jArrayLike != null && jArrayLove != null && jArrayWow != null && jArrayHaha != null && jArraySad != null && jArrayAngry != null) {
                                for (int i = 0; i < jArrayLike.length(); i++) {
                                    JSONObject reaction = new JSONObject();
                                    reaction.put("like", jArrayLike.getJSONObject(i).getJSONObject("reactions").getJSONObject("summary").getString("total_count"));
                                    reaction.put("wow", jArrayWow.getJSONObject(i).getJSONObject("reactions").getJSONObject("summary").getString("total_count"));
                                    reaction.put("haha", jArrayHaha.getJSONObject(i).getJSONObject("reactions").getJSONObject("summary").getString("total_count"));
                                    reaction.put("sad", jArraySad.getJSONObject(i).getJSONObject("reactions").getJSONObject("summary").getString("total_count"));
                                    reaction.put("angry", jArrayAngry.getJSONObject(i).getJSONObject("reactions").getJSONObject("summary").getString("total_count"));
                                    reaction.put("love", jArrayLove.getJSONObject(i).getJSONObject("reactions").getJSONObject("summary").getString("total_count"));
                                    reactions.add(reaction);
                                    listContent.add(new JSONObject(jArrayLike.getString(i)));
                                }
                                ListView listView = (ListView) findViewById(R.id.postList);
                                listView.setDividerHeight(50);
                                postCustomAdapter.setContent(listContent);
                                postCustomAdapter.setCharData(reactions);
                                Log.d("REACTION: ", listContent.size() + "");
                                Log.d("REACTION: ", reactions.size() + "");
                                listView.setAdapter(postCustomAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "posts.as(like){description,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(LIKE).limit(0).summary(true)}," +
                "posts.as(love){description,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(LOVE).limit(0).summary(true)}," +
                "posts.as(wow){description,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(WOW).limit(0).summary(true)}," +
                "posts.as(haha){description,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(HAHA).limit(0).summary(true)}," +
                "posts.as(sad){description,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(SAD).limit(0).summary(true)}," +
                "posts.as(angry){description,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(ANGRY).limit(0).summary(true)}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}