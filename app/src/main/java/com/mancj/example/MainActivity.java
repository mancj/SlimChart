package com.mancj.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.slimchart.SlimChart;
import com.mancj.slimchart.Stat;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SlimChart slimChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slimChart = findViewById(R.id.slimChart);

        //Optional - create colors array
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(46, 41, 78));
        colors.add(Color.rgb(127, 22, 101));
        colors.add(Color.rgb(217, 3, 104));
        colors.add(Color.rgb(247, 76, 110));
//        slimChart.setColors(colors);

        /* Second way to set colors:
         *
         * slimChart.setColors(
         *               Color.rgb(46, 41,78),
         *               Color.rgb(127, 22, 101),
         *               Color.rgb(217, 3, 104),
         *               Color.rgb(247, 76, 110));
         * */

        //Create array for your stats
        ArrayList<Float> stats = new ArrayList<>();
        stats.add((float) 80);
        stats.add((float) 60);
        stats.add((float) 150);
        stats.add((float) 125);
//        slimChart.setStats(stats);

        slimChart.setStacked(true);
        ArrayList<Stat> statList = new ArrayList<>();
        statList.add(new Stat((float) 80, Color.rgb(217, 3, 104)));
        statList.add(new Stat((float) 60, Color.rgb(247, 76, 110)));
        statList.add(new Stat((float) 150, Color.rgb(46, 41, 78)));
        statList.add(new Stat((float) 125, Color.rgb(127, 22, 101)));
        slimChart.setStatList(statList);

        /* Second way to set stats:
         *
         * slimChart.setStats(100, 85, 40, 25);
         * */

        //Play animation
        slimChart.setStartAnimationDuration(2000);

        //Set single color - other colors will be generated automatically
        //slimChart.setColorInt(ContextCompat.getColor(this, R.color.colorPrimary));

        slimChart.setStrokeWidth(13);
        slimChart.setText("234");
        slimChart.setTextColor(Color.WHITE);
        slimChart.setRoundEdges(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slimChart.playStartAnimation();
            }
        });
    }
}
