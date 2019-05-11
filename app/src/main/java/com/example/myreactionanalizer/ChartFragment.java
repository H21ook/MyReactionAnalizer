package com.example.myreactionanalizer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChartFragment extends Fragment {
    private JSONObject reactions;

    public ChartFragment() {
    }

    public void setChartData(JSONObject chartData) {
        this.reactions = chartData;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // CHART VIEW
        BarChart chart = getActivity().findViewById(R.id.postChart);

        ArrayList like = new ArrayList();
        ArrayList love = new ArrayList();
        ArrayList haha = new ArrayList();
        ArrayList wow = new ArrayList();
        ArrayList sad = new ArrayList();
        ArrayList angry = new ArrayList();

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
    }
}
