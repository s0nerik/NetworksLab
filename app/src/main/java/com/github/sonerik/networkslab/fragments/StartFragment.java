package com.github.sonerik.networkslab.fragments;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.f2prateek.rx.receivers.wifi.RxWifiManager;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.fragments.chat.ChatClientFragment;
import com.github.sonerik.networkslab.fragments.chat.ChatHostFragment;
import com.github.sonerik.networkslab.fragments.draw.DrawClientFragment;
import com.github.sonerik.networkslab.fragments.draw.DrawHostFragment;
import com.github.sonerik.networkslab.fragments.tic_tac_toe.TicTacToeClientFragment;
import com.github.sonerik.networkslab.fragments.tic_tac_toe.TicTacToeHostFragment;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.val;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func0;

public class StartFragment extends Fragment {

    @Bind(R.id.progressBar)
    MaterialProgressBar progressBar;

    @Bind(R.id.hostClientSelector)
    RadioGroup hostClientSelector;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btnChat)
    public void onStartChat() {
        show(ChatClientFragment::new, ChatHostFragment::new);
    }

    @OnClick(R.id.btnTicTacToe)
    public void onStartTicTacToe() {
        show(TicTacToeClientFragment::new, TicTacToeHostFragment::new);
    }

    @OnClick(R.id.btnDraw)
    public void onStartDraw() {
        show(DrawClientFragment::new, DrawHostFragment::new);
    }

    private void show(Func0<Fragment> clientFragmentCreator, Func0<Fragment> hostFragmentCreator) {
        toggleWifiAnd(() ->
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, isClient() ? clientFragmentCreator.call() : hostFragmentCreator.call())
                        .addToBackStack(null)
                        .commit()
        );
    }

    private Observable<Void> toggleWiFi() {
        val wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);

        return Observable.defer(() -> {
            if (!wifiManager.isWifiEnabled()) {
                return RxWifiManager.wifiStateChanges(getActivity().getApplicationContext())
                                    .filter(state -> state == WifiManager.WIFI_STATE_ENABLED)
                                    .take(1)
                                    .map(s -> (Void) null)
                                    .delay(2, TimeUnit.SECONDS)
                                    .timeout(10, TimeUnit.SECONDS)
                                    .doOnSubscribe(() -> wifiManager.setWifiEnabled(true));
            } else {
                return RxWifiManager.wifiStateChanges(getActivity().getApplicationContext())
                                    .filter(state -> state == WifiManager.WIFI_STATE_DISABLED)
                                    .take(1)
                                    .timeout(10, TimeUnit.SECONDS)
                                    .concatMap(s -> toggleWiFi())
                                    .map(s -> (Void) null)
                                    .doOnSubscribe(() -> wifiManager.setWifiEnabled(false));
            }
        });
    }

    private void toggleWifiAnd(Action0 action) {
        toggleWiFi().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(() -> progressBar.setVisibility(View.VISIBLE))
                    .doOnNext(v -> progressBar.setVisibility(View.GONE))
                    .subscribe(aVoid -> action.call());
    }

    private boolean isClient() {
        return hostClientSelector.getCheckedRadioButtonId() == R.id.client;
    }

}
