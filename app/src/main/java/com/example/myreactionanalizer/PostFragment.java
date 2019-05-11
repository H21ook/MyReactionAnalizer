package com.example.myreactionanalizer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostFragment  extends Fragment {

    private JSONObject postObj;

    public void setContent(JSONObject content) {
        this.postObj = content;
    }

    public PostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            try {
                postObj = new JSONObject(bundle.getString("postObj"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView postMessage = (TextView) getActivity().findViewById(R.id.postMessage);
        TextView postDomain = (TextView) getActivity().findViewById(R.id.postDomain);
        TextView postName = (TextView) getActivity().findViewById(R.id.postName);
        TextView postDescriptionBottom = (TextView) getActivity().findViewById(R.id.postDescriptionBottom);
//        VideoView postVideo = (VideoView) getActivity().findViewById(R.id.postVideo);
        ImageView postImg = (ImageView) getActivity().findViewById(R.id.postImg);

        try {
//            postMessage.setText(postObj.getString("id") + " --" + position);
            if (postObj.isNull("message") == false) {
                postMessage.setText(postObj.getString("message"));
            }

            if (postObj.isNull("attachments") == false) {
                if (postObj.getJSONObject("attachments").isNull("data") == false) {
                    for (int i = 0; i < postObj.getJSONObject("attachments").getJSONArray("data").length(); i++) {
                        if (postObj.getJSONObject("attachments").getJSONArray("data").isNull(i) == false) {
                            if (postObj.getJSONObject("attachments").getJSONArray("data").getJSONObject(i).isNull("media_type") == false) {
                                switch (postObj.getJSONObject("attachments").getJSONArray("data").getJSONObject(i).getString("media_type")) {
                                    case "album":
                                    case "photo":
                                    case "link":
                                        if (postObj.isNull("description") == false) {
                                            postDescriptionBottom.setText(postObj.getString("description"));
                                        } else postDescriptionBottom.setVisibility(View.GONE);
                                        if (postObj.isNull("full_picture") == false) {
                                            Picasso.get().load(postObj.getString("full_picture")).networkPolicy(NetworkPolicy.OFFLINE).into(postImg);
                                        } else postImg.setVisibility(View.GONE);
                                        break;
                                    case "video":
                                        if (postObj.isNull("description") == false) {
                                            postDescriptionBottom.setText(postObj.getString("description"));
                                            postDescriptionBottom.setVisibility(View.VISIBLE);
                                        } else postDescriptionBottom.setVisibility(View.GONE);
                                        if (postObj.isNull("name") == false) {
                                            postName.setText(postObj.getString("name"));
                                            postName.setVisibility(View.VISIBLE);
                                        } else postName.setVisibility(View.GONE);
//                                        Log.d("VIDEO: ", postObj.getJSONObject("attachments").getJSONArray("data").getJSONObject(i).getString("url"));
                                        if (postObj.isNull("full_picture") == false) {
                                            Picasso.get().load(postObj.getString("full_picture")).networkPolicy(NetworkPolicy.OFFLINE).into(postImg);
                                        } else postImg.setVisibility(View.GONE);
//                                        Uri uri = Uri.parse(postObj.getJSONObject("attachments").getJSONArray("data").getJSONObject(i).getString("url"));
//                                        postVideo.setVideoURI(uri);
//                                        postVideo.setMinimumHeight(postObj.getJSONObject("attachments").getJSONArray("data").getJSONObject(i).getJSONObject("media").getJSONObject("image").getInt("height"));
//                                        postVideo.setMinimumWidth(postObj.getJSONObject("attachments").getJSONArray("data").getJSONObject(i).getJSONObject("media").getJSONObject("image").getInt("width"));
//                                        postVideo.start();
//                                        MediaController ctlr = new MediaController(context);
//                                        ctlr.setMediaPlayer(postVideo);
//                                        postVideo.setMediaController(ctlr);
//                                        postVideo.requestFocus();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
