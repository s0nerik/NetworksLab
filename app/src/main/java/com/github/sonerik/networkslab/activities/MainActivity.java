package com.github.sonerik.networkslab.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.fragments.StartFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.content, new StartFragment())
                                   .commit();
    }

}
