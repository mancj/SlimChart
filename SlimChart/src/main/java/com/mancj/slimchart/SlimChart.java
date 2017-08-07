package com.mancj.slimchart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Collections;

public class SlimChart extends View {
    private static final float DEFAULT_SIZE = 100;
    private static final float FULL_CIRCLE_ANGLE = 360f;

    private RectF chartRect;
    private int strokeWidth = 6;
    private int defaultSize;
    private int color;
    private int[] colors;
    private float density;
    private boolean roundEdges;
    private int animDuration = 1000;
    private int textColor;
    private ArrayList<Float> stats;
    private String text;
    private float maxStat;

    public SlimChart(Context context) {
        this(context, null);
    }

    public SlimChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlimChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SlimChart);
        try {
            this.density = getContext().getResources().getDisplayMetrics().density;
            this.defaultSize = (int) (DEFAULT_SIZE * density);
            this.strokeWidth = (int) typedArray.getDimension(R.styleable.SlimChart_strokeWidth, strokeWidth * density);
            this.roundEdges = typedArray.getBoolean(R.styleable.SlimChart_roundedEdges, false);
            this.text = typedArray.getString(R.styleable.SlimChart_text);
            this.textColor = typedArray.getColor(R.styleable.SlimChart_textColor, Color.WHITE);

            int defColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
            this.color = typedArray.getColor(R.styleable.SlimChart_defaultColor, defColor);

            chartRect = new RectF();
        }finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        switch (widthSpec){
            case MeasureSpec.AT_MOST:
                width = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                width = getMeasuredWidth();
                break;
        }

        switch (heightSpec){
            case MeasureSpec.AT_MOST:
                height = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                height = getMeasuredHeight();
                break;
        }

        int chartSize = Math.min(width, height);
        int centerX = width / 2 - chartSize / 2;
        int centerY = height / 2 - chartSize / 2;
        int strokeHalf = strokeWidth/2;

        chartRect.set(centerX + strokeHalf, centerY + strokeHalf, centerX + chartSize - strokeHalf, centerY + chartSize - strokeHalf);
        setMeasuredDimension(width, height);
    }

    public void playStartAnimation(){
        float from = 0;
        float to = 1;
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(animDuration);

        final ArrayList<Float> animated = new ArrayList<>(stats);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                for (int i = 0; i < stats.size(); i++) {
                    stats.set(i, animated.get(i) * val);
                }
                invalidate();
            }
        });
        animator.start();
    }

    public void setStartAnimationDuration(int duration){
        this.animDuration = duration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (stats != null){
            if (colors == null) colors = createColors();

            if (colors.length != stats.size()) {
                Log.e("SlimChart", "Stats and colors have different lengths, will be used default colors...");
                colors = createColors();
            }

            for (int i = 0; i < stats.size(); i++) {
                drawChart(canvas, colors[i], calculatePercents(stats.get(i)));
            }
        }else {
            drawChart(canvas, color, calculatePercents(FULL_CIRCLE_ANGLE));
        }
        drawText(canvas);
    }

    private float calculatePercents(float degree){
        return degree * FULL_CIRCLE_ANGLE / maxStat;
    }

    private int[] createColors(){
        int chartsCount = stats.size();
        float add = .9f / chartsCount;
        int[] colors = new int[chartsCount];
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = .1f;
        for (int i = 0; i < chartsCount; i++) {
            hsv[2] += add;
            colors[i] = Color.HSVToColor(hsv);
        }
        return colors;
    }

    private void drawText(Canvas canvas){
        if (this.text == null){
            return;
        }
        Paint paint = new Paint();
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        paint.setTextSize(chartRect.height()/3);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        paint.measureText(text);
        canvas.drawText(text, getWidth()/2-textBounds.right/2, getHeight()/2 + textBounds.height()/2, paint);
    }

    private void drawChart(Canvas canvas, int color, float degree){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        if (roundEdges){
            paint.setStrokeCap(Paint.Cap.ROUND);
        }
        canvas.drawArc(chartRect, 90, degree, false, paint);
    }

    public void setRoundEdges(boolean roundEdges) {
        this.roundEdges = roundEdges;
        invalidate();
    }

    public boolean isRoundEdgesEnabled() {
        return roundEdges;
    }

    public void setStats(ArrayList<Float> stats) {
        Collections.sort(stats, Collections.<Float>reverseOrder());
        this.stats = stats;
        maxStat = stats.get(0); //First stat is the largest, save for arc calculations.
        invalidate();
    }

    public ArrayList<Float> getStats() {
        return stats;
    }

    public void setColors(int... colors) {
        this.colors = colors;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColorInt(@ColorInt int color) {
        this.colors = null;
        this.color = color;
        invalidate();
    }

    public void setColorRes(@ColorRes int colorResId) {
        setTextColorInt(ContextCompat.getColor(getContext(), colorResId));
    }

    public int getColor() {
        return color;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = (int) (strokeWidth * density);
        invalidate();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setText(@StringRes int textIdRes){
        setText(getContext().getString(textIdRes));
    }

    public String getText() {
        return text;
    }

    public void setTextColorInt(@ColorInt int textColorInt) {
        this.textColor = textColorInt;
        invalidate();
    }

    public void setTextColorRes(@ColorRes int textColorResId){
        setTextColorInt(ContextCompat.getColor(getContext(), textColorResId));
    }

    public int getTextColor() {
        return textColor;
    }
}
