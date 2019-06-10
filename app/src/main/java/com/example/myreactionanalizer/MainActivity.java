package com.example.myreactionanalizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
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
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<JSONObject> listContent = new ArrayList<>();
    private ArrayList<JSONObject> reactions = new ArrayList<>();
    private CustomAdapter postCustomAdapter = new CustomAdapter(this);
    private WebAppInterface webAppInterface;
    private String userID;
    private DatabaseController dbCtrl;
    private  android.text.format.DateFormat df;

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

        TextView firstName = (TextView) navHeaderView.findViewById(R.id.firstName);
        final TextView age = (TextView) navHeaderView.findViewById(R.id.age);
        final TextView gender = (TextView) navHeaderView.findViewById(R.id.gender);
        final TextView birthDay = (TextView) navHeaderView.findViewById(R.id.birthDay);
        final CircleImageView userImg = (CircleImageView) navHeaderView.findViewById((R.id.userImg));

        Bundle bundle = getIntent().getExtras();

        firstName.setText(bundle.getString("userLastName") +" "+ bundle.getString("userFirstName"));

        userID = bundle.getString("userID");


        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Picasso.get()
                            .load(object.getJSONObject("picture").getJSONObject("data").getString("url").toString())
                            .into(userImg);
                    if(!object.getJSONObject("age_range").getString("min").isEmpty()) {
                        age.setText(object.getJSONObject("age_range").getString("min"));
                    }
                    if(!object.getString("gender").isEmpty()) {
                        if (object.getString("gender").equalsIgnoreCase("male") == true) {
                            gender.setText("Эр");
                        } else {
                            gender.setText("Эм");
                        }
                    }
                    if(!object.getString("birthday").isEmpty()) {
                        birthDay.setText(object.getString("birthday"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.height(200),age_range,birthday,gender,first_name,last_name");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();

        Button refreshBtn = (Button) findViewById(R.id.refBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
            }
        });
        //WebAppInterface
        WebView webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/index.html");
        webAppInterface =  new WebAppInterface(MainActivity.this);
        webView.addJavascriptInterface(webAppInterface, "AndroidInterface");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
        //WebAppInterface

        dbCtrl = new DatabaseController(MainActivity.this, userID);

        df = new android.text.format.DateFormat();
        dbCtrl.readPosts(String.valueOf(df.format("yyyy-MM-dd", new Date())));

        refresh();
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        } else if(id == R.id.nav_find) {
            Intent find = new Intent(MainActivity.this, FindActivity.class);
            startActivity(find);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void refresh() {
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
                                webAppInterface.setPostData(listContent);
                                webAppInterface.setChartData(reactions);
                                dbCtrl.readAllDayPosts();
                                dbCtrl.addAndUpdatePosts(listContent,reactions);
//                                ListView listView = (ListView) findViewById(R.id.postList);
//                                listView.setDividerHeight(50);
//                                postCustomAdapter.setContent(listContent);
//                                postCustomAdapter.setCharData(reactions);
//                                listView.setAdapter(postCustomAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -30);

        String tTime = ""+cal.get(Calendar.YEAR);

        if(cal.get(Calendar.MONTH) + 1 < 10)
            tTime += "-0" + (cal.get(Calendar.MONTH) + 1);
        else
            tTime += "-" + (cal.get(Calendar.MONTH) + 1);

        if(cal.get(Calendar.DAY_OF_MONTH) < 10)
            tTime += "-0" + cal.get(Calendar.DAY_OF_MONTH);
        else
            tTime += "-" + cal.get(Calendar.DAY_OF_MONTH);

        parameters.putString("fields", "posts.as(like).since("+tTime+").limit(100){description,permalink_url,type,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(LIKE).limit(0).summary(true)}," +
                "posts.as(love).since("+tTime+").limit(100){description,permalink_url,type,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(LOVE).limit(0).summary(true)}," +
                "posts.as(wow).since("+tTime+").limit(100){description,permalink_url,type,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(WOW).limit(0).summary(true)}," +
                "posts.as(haha).since("+tTime+").limit(100){description,permalink_url,type,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(HAHA).limit(0).summary(true)}," +
                "posts.as(sad).since("+tTime+").limit(100){description,permalink_url,type,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(SAD).limit(0).summary(true)}," +
                "posts.as(angry).since("+tTime+").limit(100){description,permalink_url,type,message,source,created_time,name,attachments{description,media_type,media,url},picture,caption,from,full_picture,reactions.type(ANGRY).limit(0).summary(true)}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void test() {

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //handle the result\
                        try {
                            Log.d("MYLOG", object.toString());
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "/feed");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
