package com.mancj.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mancj.slimchart.SlimChart;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlimChart slimChart = (SlimChart) findViewById(R.id.slimChart);

        //Optional - create colors array
        int[] colors = new int[4];
        colors[0] = Color.rgb(46, 41,78);
        colors[1] = Color.rgb(127, 22, 101);
        colors[2] = Color.rgb(217, 3, 104);
        colors[3] = Color.rgb(247, 76, 110);
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
        final float[] stats = new float[4];
        stats[0] = 100;
        stats[1] = 85;
        stats[2] = 40;
        stats[3] = 25;
        slimChart.setStats(stats);

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
        slimChart.setTextColorInt(Color.WHITE);
        slimChart.setRoundEdges(true);

    }
}
