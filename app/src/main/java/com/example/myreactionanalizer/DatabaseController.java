package com.example.myreactionanalizer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseController {
    public static String TAG = "MY LOG";
    Context mContext;
    FirebaseFirestore db;
    ArrayList<JSONObject> myposts = new ArrayList<JSONObject>();
    CollectionReference postsCol;
    JSONObject mypost;
    public DatabaseController(Context context) {
        this.mContext = context;
        this.init();
    }

    public void init() {
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setApplicationId(applicationID) // Required for Analytics.
//                .setApiKey(apiKey) // Required for Auth.
//                .build();
//        FirebaseApp.initializeApp(mContext, options);
        db = FirebaseFirestore.getInstance();
        postsCol = db.collection("cities");
    }

    public void addPosts(ArrayList<JSONObject> posts, ArrayList<JSONObject> reactions) throws JSONException {
        // Create a new user with a first and last name
        for (int i = 0; i < posts.size(); i++) {
            if(myposts.size() > 0) {
                for (int j = 0; j < myposts.size(); j++) {
                    readPost(myposts.get(j).getString("id"));
                    if (mypost != null) {
                        Date now = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(now);
                        Date postDate = new Date(mypost.getString("timestamp"));
                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(postDate);
                        if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) {

                        } else {
                            Map<String, Object> post = new HashMap<>();

                            post.put("like", reactions.get(i).getString("like"));
                            post.put("love", reactions.get(i).getString("love"));
                            post.put("sad", reactions.get(i).getString("sad"));
                            post.put("angry", reactions.get(i).getString("angry"));
                            post.put("wow", reactions.get(i).getString("wow"));
                            post.put("haha", reactions.get(i).getString("haha"));
                            Date date = new Date();
                            post.put("timestamp", new Timestamp(date.getTime()));
                            postsCol.document(posts.get(i).getString("id")).set(post);
                        }
                    }
                }


// Add a new document with a generated ID
//                postsCol.add(post)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error adding document", e);
//                            }
//                        });

            } else {
                Map<String, Object> post = new HashMap<>();

                post.put("like", reactions.get(i).getString("like"));
                post.put("love", reactions.get(i).getString("love"));
                post.put("sad", reactions.get(i).getString("sad"));
                post.put("angry", reactions.get(i).getString("angry"));
                post.put("wow", reactions.get(i).getString("wow"));
                post.put("haha", reactions.get(i).getString("haha"));
                Date date = new Date();
                post.put("timestamp", new Timestamp(date.getTime()));
                postsCol.document(posts.get(i).getString("id")).set(post);
            }
        }
    }

    public void readPosts() {
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                myposts.add(new JSONObject(document.getData()));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void readPost(String id) {
        final DocumentReference docRef = db.collection("posts").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    mypost = new JSONObject(snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }
}
