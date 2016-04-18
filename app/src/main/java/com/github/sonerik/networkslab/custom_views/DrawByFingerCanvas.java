package com.github.sonerik.networkslab.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.beans.draw.Point;

import java.util.TreeSet;

import rx.Observable;
import rx.subjects.PublishSubject;

public class DrawByFingerCanvas extends View {

    private PublishSubject<Point> pointsSubject = PublishSubject.create();

    private Paint brush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();

    private float density;

    private TreeSet<Point> pointsQueue = new TreeSet<>((lhs, rhs) -> lhs.index - rhs.index);
    private int lastAddedPointIndex = -1;

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

        p.index = lastAddedPointIndex+1;

        addPoint(p);
        pointsSubject.onNext(p);

        return true;
    }

    public void addPoint(Point p) {
        Log.d(Constants.LOG_TAG, "addPoint::"+p.index+"::("+p.type+"): "+p.x+", "+p.y);
        if (p.index == lastAddedPointIndex + 1) {
            switch (p.type) {
                case DOWN:
                    path.moveTo(p.x * density,p.y * density);
                    break;
                case MOVE:
                    path.lineTo(p.x * density, p.y * density);
                    break;
            }

            if (pointsQueue.size() > 0 && pointsQueue.first().index == lastAddedPointIndex + 1) {
                addPoint(pointsQueue.pollFirst());
            }

            lastAddedPointIndex++;

            invalidate();
        } else {
            pointsQueue.add(p);
        }
    }

    public void clear() {
        path.reset();
        invalidate();
    }
}