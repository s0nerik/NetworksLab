package com.github.sonerik.networkslab.fragments.draw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.draw.DrawMessage;
import com.github.sonerik.networkslab.beans.draw.Point;
import com.github.sonerik.networkslab.custom_views.DrawByFingerCanvas;
import com.github.sonerik.networkslab.fragments.base.NetworkFragment;
import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.IndicatorMode;
import com.pavelsikun.vintagechroma.colormode.ColorMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.val;
import rx.Subscription;

public abstract class DrawFragment extends NetworkFragment {
    @Bind(R.id.canvas)
    DrawByFingerCanvas canvas;

    @Bind(R.id.drawArea)
    View drawArea;

    @Bind(R.id.thicknessSeekBar)
    SeekBar thicknessSeekBar;

    private Subscription pointsSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_draw, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        thicknessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                canvas.setSelectedThickness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pointsSubscription = canvas.getPointsObservable()
                                   .buffer(1)
                                   .onBackpressureBuffer()
                                   .subscribe(this::onNewPointsBufferAvailable);
    }

    @Override
    public void onDestroyView() {
        pointsSubscription.unsubscribe();

        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDataReceived(Object o) {
        super.onDataReceived(o);

        val msg = DrawMessage.fromJson((String)o);
        if (msg != null) {
            switch (msg.type) {
                case NEW_POINTS:
                    for (Point point : msg.points) {
                        canvas.addPoint(point);
                    }
                    break;
                case CLEAR:
                    canvas.clear();
                    break;
            }
        }
    }

    @OnClick(R.id.btnClear)
    public void onClear() {
        Log.d(Constants.LOG_TAG, "onClear");
        canvas.clear();
        onCanvasCleared();
    }

    @OnClick(R.id.btnSelectColor)
    public void onSelectColor() {
        new ChromaDialog.Builder()
                .initialColor(canvas.getSelectedColor())
                .colorMode(ColorMode.RGB) // RGB, ARGB, HVS, CMYK, CMYK255, HSL
                .indicatorMode(IndicatorMode.DECIMAL)
                .onColorSelected(color -> canvas.setSelectedColor(color))
                .create()
                .show(getChildFragmentManager(), "ChromaDialog");
    }

    @OnClick(R.id.btnCircle)
    public void onCircleSelected() {
        canvas.setSelectedFigure(Point.Figure.CIRCLE);
    }

    @OnClick(R.id.btnCube)
    public void onCubeSelected() {
        canvas.setSelectedFigure(Point.Figure.CUBE);
    }

    @OnClick(R.id.btnLine)
    public void onLineSelected() {
        canvas.setSelectedFigure(Point.Figure.LINE);
    }

    @Override
    protected String getServiceName() {
        return Constants.SERVICE_DRAW;
    }

    @Override
    protected int getDefaultServicePort() {
        return Constants.SERVICE_DRAW_DEFAULT_PORT;
    }

    protected abstract void onNewPointsBufferAvailable(List<Point> buffer);
    protected abstract void onCanvasCleared();
}
