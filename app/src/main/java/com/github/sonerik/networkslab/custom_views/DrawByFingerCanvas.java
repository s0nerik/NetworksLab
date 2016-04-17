package com.github.sonerik.networkslab.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.sonerik.networkslab.beans.draw.Point;

import rx.Observable;
import rx.subjects.PublishSubject;

public class DrawByFingerCanvas extends View {

    private PublishSubject<Point> pointsSubject = PublishSubject.create();

    private Paint brush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();

    private float density;

    public DrawByFingerCanvas(Context context) {
        super(context);
        init();
    }

    public DrawByFingerCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawByFingerCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;

        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeWidth(5);

        density = getResources().getDisplayMetrics().density;
    }

    public Observable<Point> getPointsObservable() {
        return pointsSubject;
    }

    @Override
    protected void onDraw(Canvas c) {
        c.drawPath(path, brush);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point p = new Point();
        p.x = event.getX() / density;
        p.y = event.getY() / density;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                p.type = Point.Type.DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                p.type = Point.Type.MOVE;
                break;
            default:
                return false;
        }

        addPoint(p);
        pointsSubject.onNext(p);

        return true;
    }

    public void addPoint(Point p) {
        switch (p.type) {
            case DOWN:
                path.moveTo(p.x * density,p.y * density);
                break;
            case MOVE:
                path.lineTo(p.x * density, p.y * density);
                break;
        }

        invalidate();
    }

    public void clear() {
        path.reset();
        invalidate();
    }
}