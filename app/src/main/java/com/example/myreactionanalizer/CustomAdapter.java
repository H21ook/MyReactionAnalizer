package com.example.myreactionanalizer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

    boolean isBackShow=false;
    private Context context;

    ArrayList<JSONObject> content;
    ArrayList<JSONObject> chartData;
    FragmentManager fragmentManager;

    public CustomAdapter(Context context) {
        this.context = context;
    }

    public void setContent(ArrayList<JSONObject> content) { this.content = content; }

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.post_list_view , null);

        Button flipbtn = (Button) item.findViewById(R.id.flipBtn);

        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        PostFragment postFragment = new PostFragment();
        postFragment.setContent(getItem(position));
//
//
//        final ChartFragment chartFragment = new ChartFragment();
//        chartFragment.setChartData(this.chartData.get(position));
//
        Log.d("ASDAF",  getItem(position).toString());
        fragmentManager.beginTransaction().replace(R.id.post, postFragment).commit();
//
        flipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ASDAF",  position + "");
//                if(isBackShow){
//                    fragmentManager.beginTransaction().setCustomAnimations(R.animator.card_flip_right_in,R.animator.card_flip_right_out)
//                            .replace(R.id.post, chartFragment).commit();
//                    isBackShow=false;
//                }else{
//                    fragmentManager.beginTransaction().setCustomAnimations(R.animator.card_flip_right_in,R.animator.card_flip_right_out)
//                            .replace(R.id.post, postFragment).commit();
//                    isBackShow=true;
//                }
            }
        });
        return item;
    }

}
