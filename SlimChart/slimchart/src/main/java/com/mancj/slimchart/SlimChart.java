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
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class SlimChart extends View {
    private static final float DEFAULT_SIZE = 100;
    private static final float FULL_CIRCLE_ANGLE = 360f;
    private int strokeWidth = 6;
    private RectF chartRect;
    private int[] colors;
    private int color;
//    private int chartSize;
    private int defaultSize;
    private float stats[];
    private String text;
    private float density;
    private boolean roundEdges;
    private int animDuration = 1000;
    private int textColor;

    public SlimChart(Context context) {
        super(context);
        init(null);
    }

    public SlimChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SlimChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SlimChart);

        this.density = getContext().getResources().getDisplayMetrics().density;
        this.defaultSize = (int) (DEFAULT_SIZE * density);
        this.strokeWidth = (int) typedArray.getDimension(R.styleable.SlimChart_strokeWidth, strokeWidth * density);
        this.roundEdges = typedArray.getBoolean(R.styleable.SlimChart_roundedEdges, false);
        this.text = typedArray.getString(R.styleable.SlimChart_text);
        this.textColor = typedArray.getColor(R.styleable.SlimChart_textColor, Color.WHITE);

        int defColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        this.color = typedArray.getColor(R.styleable.SlimChart_defaultColor, defColor);

        typedArray.recycle();
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
        int centerX = width/2 - chartSize /2;
        int centerY = height/2 - chartSize /2;
        int strokeHalf = strokeWidth/2;

        chartRect = new RectF(centerX + strokeHalf, centerY + strokeHalf, centerX + chartSize - strokeHalf, centerY + chartSize - strokeHalf);
        setMeasuredDimension(width, height);
    }

    public void playStartAnimation(){
        float from = 0;
        float to = 100;
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(animDuration);

        final float[] animated = new float[stats.length];
        for (int i = 0; i < stats.length; i++) {
            animated[i] = stats[i];
        }
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                for (int i = 0; i < stats.length; i++) {
                    stats[i] = calculatePercents(animated[i], val);
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
        if (stats != null)
        {
            for (int i = 0; i < stats.length; i++) {
                if (colors == null)
                {
                    colors = createColors();
                }
                drawChart(canvas, colors[i], calculatePercents(FULL_CIRCLE_ANGLE, stats[i]));
            }
        }else {
            drawChart(canvas, color, calculatePercents(FULL_CIRCLE_ANGLE, 100));
        }
//        createColors();
        drawText(canvas);
    }

    private int[] createColors(){
        int chartsCount = stats.length;
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
        if (this.text == null)
        {
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

    private float calculatePercents(float a1, float a2){
        float a = a1 * a2 / 100;
        return a;
    }

    private void drawChart(Canvas canvas, int color, float radius){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        if (roundEdges)
        {
            paint.setStrokeCap(Paint.Cap.ROUND);
        }
        canvas.drawArc(chartRect, 90, radius, false, paint);
    }

    public void setRoundEdges(boolean roundEdges) {
        this.roundEdges = roundEdges;
        invalidate();
    }

    public boolean isRoundEdgesEnabled() {
        return roundEdges;
    }

    public void setStats(float[] stats) {
        this.stats = stats;
        invalidate();
    }

    public float[] getStats() {
        return stats;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColor(int color) {
        this.colors = null;
        this.color = color;
        invalidate();
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

    public String getText() {
        return text;
    }

    public void setTextColor(int textColorResId) {
        this.textColor = ContextCompat.getColor(getContext(), textColorResId);
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }
}
