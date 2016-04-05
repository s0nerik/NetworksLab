package com.github.sonerik.networkslab.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.events.DeviceChosenEvent;
import com.github.sonerik.networkslab.fragments.StartFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.content, new StartFragment())
                                   .commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(DeviceChosenEvent e) {
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.content, new StartFragment())
                                   .commit();
    }
}
