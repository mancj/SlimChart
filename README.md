<img src="https://github.com/mancj/SlimChart/blob/master/art/app_logo.png" width="100"> 
# SlimChart
SlimChart is a light and easy to use chart library for Android.
Using SlimChart you will be able to create a stylish chart for your stats

----------
<img src="https://github.com/mancj/SlimChart/blob/master/art/slim-chart-screencast.gif" width="300"> 
<img src="https://github.com/mancj/SlimChart/blob/master/art/device-2016-10-31-215202.png" width="300"> 

## To get a SlimChart project into your build:

**Step 1.** Add it in your root build.gradle at the end of repositories:
```xml 
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
**Step 2.** Add the dependency
```xml
dependencies {
    compile 'com.github.mancj:SlimChart:0.1.2'
}
```


## Usage examples

**xml:**
```xml
<com.mancj.slimchart.SlimChart
        android:id="@+id/slimChart" 
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:text="234kg"
        app:textColor="@color/colorAccent"
        app:strokeWidth="8dp"
        app:roundedEdges="true"/>
```

**java:**
```java
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
        slimChart.setColors(colors);

        //Create array for your stats
        final float[] stats = new float[4];
        stats[0] = 100;
        stats[1] = 85;
        stats[2] = 40;
        stats[3] = 25;
        slimChart.setStats(stats);

        //Play animation
        slimChart.setStartAnimationDuration(2000);

        //Set single color - other colors will be generated automatically
        slimChart.setColor(ContextCompat.getColor(this, R.color.colorPrimary));

        slimChart.setStrokeWidth(13);
        slimChart.setText("234");
        slimChart.setTextColor(Color.WHITE);
        slimChart.setRoundEdges(true);
    }
```
