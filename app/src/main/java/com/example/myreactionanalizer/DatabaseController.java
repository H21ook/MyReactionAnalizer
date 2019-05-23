package com.example.myreactionanalizer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatabaseController {
    public static String TAG = "MY LOG";
    private Context mContext;
    private FirebaseFirestore db;
    private ArrayList<JSONObject> myposts = new ArrayList<JSONObject>();
    private CollectionReference postsCol;

    public DatabaseController(Context context) {
        this.mContext = context;
        init();
    }

    public void init() {
        db = FirebaseFirestore.getInstance();
        postsCol = db.collection("posts");
    }

    //add update
    public void addAndUpdatePosts(ArrayList<JSONObject> posts, ArrayList<JSONObject> reactions) throws JSONException {

        Map<String, Object> dayData = new HashMap<>();

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String todayDate = String.valueOf(df.format("yyyy-MM-dd", new Date()));
        String todayTime = "";


        ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
        for (int i = 0; i < posts.size(); i++) {
            todayTime = String.valueOf(df.format("yyyy-MM-dd, HH:mm:ss", new Date()));
            JSONObject createPost = new JSONObject();;
            createPost.put("id", posts.get(i).getString("id"));
            createPost.put("like", reactions.get(i).getString("like"));
            createPost.put("love", reactions.get(i).getString("love"));
            createPost.put("sad", reactions.get(i).getString("sad"));
            createPost.put("angry", reactions.get(i).getString("angry"));
            createPost.put("wow", reactions.get(i).getString("wow"));
            createPost.put("haha", reactions.get(i).getString("haha"));
            createPost.put("timestamp", todayTime);
            createPost.put("date", todayDate);
            temp.add(createPost);
        }
        if(myposts.size() > 0) {
            for (int j = 0; j < myposts.size(); j++) {
                JSONObject o = new JSONObject(myposts.get(j).getString("reactions").toString());
                JSONObject createPost = new JSONObject();
                createPost.put("id", myposts.get(j).getString("id"));
                createPost.put("like", o.getString("like"));
                createPost.put("love", o.getString("love"));
                createPost.put("sad", o.getString("sad"));
                createPost.put("angry", o.getString("angry"));
                createPost.put("wow", o.getString("wow"));
                createPost.put("haha", o.getString("haha"));
                createPost.put("timestamp", o.getString("timestamp"));
                createPost.put("date", o.getString("date"));
                temp.add(createPost);
            }
        }

        for (int i = 0; i < temp.size(); i++)
        {
            // Check if the picked element is already printed
            int j;
            for (j=0; j<i; j++) {
                if (temp.get(i).getString("id") == temp.get(j).getString("id"))
                    break;
            }

            // If not printed earlier, then print it
            if (i == j) {
                Map<String, Object> createPost = new HashMap<>();
                createPost.put("id", temp.get(i).getString("id"));
                createPost.put("like", temp.get(i).getString("like"));
                createPost.put("love", temp.get(i).getString("love"));
                createPost.put("sad", temp.get(i).getString("sad"));
                createPost.put("angry", temp.get(i).getString("angry"));
                createPost.put("wow", temp.get(i).getString("wow"));
                createPost.put("haha", temp.get(i).getString("haha"));
                createPost.put("timestamp", temp.get(i).getString("timestamp"));
                createPost.put("date", temp.get(i).getString("date"));
                dayData.put(temp.get(i).getString("id"), createPost);
            }
        }

        postsCol.document(todayDate).set(dayData);
    }

    //read all day post data
    public void readAllDayPosts() {
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                myposts.add(new JSONObject(document.getData()));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //read one day post data
    public void readPosts(String date) {
        final DocumentReference docRef = db.collection("posts").document(date);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    JSONObject jo = new JSONObject(snapshot.getData());
                    Iterator iter = jo.keys();
                    while(iter.hasNext()){
                        String key = (String)iter.next();
                        try {
                            String dataText = "";
                            dataText += "{\"id\":\"" + key + "\", \"reactions\":";
                            dataText += jo.getString(key) + "}";
                            JSONObject o = new JSONObject(dataText);
                            myposts.add(o);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }
}
