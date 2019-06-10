package com.example.myreactionanalizer;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter  {

    private Context context;
    ArrayList<JSONObject> content;
    ArrayList<JSONObject> chartData;

    public CustomAdapter(Context context) {
        this.context = context;
    }

    public void setContent(ArrayList<JSONObject> content) {
        this.content = content;
    }

    public void setCharData(ArrayList<JSONObject> chartData) {
        this.chartData = chartData;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        FindViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        item = inflater.inflate(R.layout.post_list_view , null);

        holder = new FindViewHolder();
        TextView postMessage = (TextView) item.findViewById(R.id.postMessage);
        TextView postDomain = (TextView) item.findViewById(R.id.postDomain);
        TextView postName = (TextView) item.findViewById(R.id.postName);
        TextView postDescriptionBottom = (TextView) item.findViewById(R.id.postDescriptionBottom);
        VideoView postVideo = (VideoView) item.findViewById(R.id.postVideo);
        ImageView postImg = (ImageView) item.findViewById(R.id.postImg);

        // CHART VIEW
        BarChart chart = item.findViewById(R.id.postChart);

        ArrayList like = new ArrayList();
        ArrayList love = new ArrayList();
        ArrayList haha = new ArrayList();
        ArrayList wow = new ArrayList();
        ArrayList sad = new ArrayList();
        ArrayList angry = new ArrayList();

        JSONObject reactions = chartData.get(position);
        try {
            if(reactions.isNull("like") == false) {
                like.add(new BarEntry(1, reactions.getInt("like")));
            }
            if(reactions.isNull("love") == false) {
                love.add(new BarEntry(2, reactions.getInt("love")));
            }
            if(reactions.isNull("haha") == false) {
                haha.add(new BarEntry(3, reactions.getInt("haha")));
            }
            if(reactions.isNull("wow") == false) {
                wow.add(new BarEntry(4, reactions.getInt("wow")));
            }
            if(reactions.isNull("sad") == false) {
                sad.add(new BarEntry(5, reactions.getInt("sad")));
            }
            if(reactions.isNull("angry") == false) {
                angry.add(new BarEntry(6, reactions.getInt("angry")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BarDataSet likeDataset = new BarDataSet(like, "LIKE");
        BarDataSet loveDataset = new BarDataSet(love, "LOVE");
        BarDataSet hahaDataset = new BarDataSet(haha, "HAHA");
        BarDataSet wowDataset = new BarDataSet(wow, "WOW");
        BarDataSet sadDataset = new BarDataSet(sad, "SAD");
        BarDataSet angryDataset = new BarDataSet(angry, "ANGRY");


        chart.animateY(3000);

        Description d = new Description();
        d.setText("");
        chart.setDescription(d);
        chart.setBackgroundColor(Color.WHITE);

        BarData data = new BarData(likeDataset, loveDataset, hahaDataset, wowDataset, sadDataset,angryDataset);

        likeDataset.setColors(Color.rgb(97, 157, 232));
        loveDataset.setColors(Color.rgb(222, 62, 103));
        hahaDataset.setColors(Color.rgb(241, 215, 120));
        sadDataset.setColors(Color.rgb(0, 128, 248));
        wowDataset.setColors(Color.rgb(248, 123, 40));
        angryDataset.setColors(Color.rgb(206, 30, 73));

        chart.setData(data);

        // CHART VIEW

        JSONObject postObj = content.get(position);

        try {
//            postMessage.setText(postObj.getString("id") + " --" + position);
            if(postObj.isNull("message") == false) {
                postMessage.setText(postObj.getString("message"));
            }

            if(postObj.isNull("attachments") == false) {
                if(postObj.getJSONObject("attachments").isNull("data") == false) {
                    for(int i = 0; i < postObj.getJSONObject("attachments").getJSONArray("data").length(); i++) {
                        if (postObj.getJSONObject("attachments").getJSONArray("data").isNull(i) == false) {
                            if (postObj.getJSONObject("attachments").getJSONArray("data").getJSONObject(i).isNull("media_type") == false) {
                                switch (postObj.getJSONObject("attachments").getJSONArray("data").getJSONObject(i).getString("media_type")) {
                                    case "album":
                                    case "photo":
                                    case "link":
                                        if(postObj.isNull("description") == false) {
                                            postDescriptionBottom.setText(postObj.getString("description"));
                                        } else postDescriptionBottom.setVisibility(View.GONE);
                                        if(postObj.isNull("full_picture") == false) {
                                            Picasso.get().load(postObj.getString("full_picture")).networkPolicy(NetworkPolicy.OFFLINE).into(postImg);
                                        } else postImg.setVisibility(View.GONE);
                                        break;
                                    case "video":
                                        if(postObj.isNull("description") == false) {
                                            postDescriptionBottom.setText(postObj.getString("description"));
                                            postDescriptionBottom.setVisibility(View.VISIBLE);
                                        } else postDescriptionBottom.setVisibility(View.GONE);
                                        if(postObj.isNull("name") == false) {
                                            postName.setText(postObj.getString("name"));
                                            postName.setVisibility(View.VISIBLE);
                                        } else postName.setVisibility(View.GONE);
                                        Log.d("VIDEO: ", postObj.getJSONObject("attachments").getJSONArray("data").getJSONObject(i).getString("url"));
                                        if(postObj.isNull("full_picture") == false) {
                                            Picasso.get().load(postObj.getString("full_picture")).networkPolicy(NetworkPolicy.NO_CACHE).into(postImg);
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

        return item;
    }
}
