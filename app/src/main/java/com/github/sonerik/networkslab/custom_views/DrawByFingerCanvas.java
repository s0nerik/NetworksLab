package com.github.sonerik.networkslab.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.sonerik.networkslab.beans.draw.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import rx.Observable;
import rx.subjects.PublishSubject;

public class DrawByFingerCanvas extends View {

    private PublishSubject<Point> pointsSubject = PublishSubject.create();

    private Paint brush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<Pair<Path, Paint>> paths = new ArrayList<>();

    private float density;

    private TreeSet<Point> points = new TreeSet<>((lhs, rhs) -> lhs.index - rhs.index);
    private int lastAddedPointIndex = -1;

    private int selectedColor = Color.BLACK;
    private int selectedThickness = 5;

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
        density = getResources().getDisplayMetrics().density;
        initBrush(brush, null);
    }

    private void initBrush(Paint brush, Point point) {
        if (point == null) {
            brush.setStyle(Paint.Style.STROKE);
            brush.setStrokeWidth(selectedThickness);
            brush.setColor(selectedColor);
        } else {
            brush.setStyle(Paint.Style.STROKE);
            brush.setStrokeWidth(point.thickness);
            brush.setColor(point.color);
        }
    }

    public Observable<Point> getPointsObservable() {
        return pointsSubject;
    }

    @Override
    protected void onDraw(Canvas c) {
        for (Pair<Path, Paint> path : paths) {
            c.drawPath(path.first, path.second);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point p = new Point();
        p.x = event.getX() / density;
        p.y = event.getY() / density;

        p.color = selectedColor;
        p.thickness = selectedThickness;

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

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setSelectedThickness(int selectedThickness) {
        this.selectedThickness = selectedThickness;
    }

    public int getSelectedThickness() {
        return selectedThickness;
    }

    public synchronized void addPoint(Point p) {
        points.add(p);

        if (!points.isEmpty() && points.last().index > lastAddedPointIndex)
            lastAddedPointIndex = points.last().index;

        paths.clear();
        for (Point point : points) {
            switch (point.type) {
                case DOWN:
                    Paint brush = new Paint(Paint.ANTI_ALIAS_FLAG);
                    initBrush(brush, point);
                    Pair<Path, Paint> pair = new Pair<>(new Path(), brush);
                    paths.add(pair);
                    pair.first.moveTo(point.x * density, point.y * density);
                    break;
                case MOVE:
                    if (paths.size() > 0) paths.get(paths.size() - 1).first.lineTo(point.x * density, point.y * density);
                    break;
            }
        }
        invalidate();
    }

    public void clear() {
        points.clear();
        paths.clear();
        invalidate();
    }
}