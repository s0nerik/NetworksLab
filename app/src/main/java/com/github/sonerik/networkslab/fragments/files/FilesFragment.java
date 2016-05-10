package com.github.sonerik.networkslab.fragments.files;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.files.FileMessage;
import com.github.sonerik.networkslab.fragments.base.NetworkFragment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.val;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class FilesFragment extends NetworkFragment {
    @Bind(R.id.btnChooseFile)
    Button btnChooseFile;

    @Bind(R.id.layout)
    View layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_files, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDataReceived(Object o) {
        super.onDataReceived(o);

        val msg = FileMessage.fromJson((String)o);
        if (msg != null) {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/NetworksLab");
            dir.mkdirs();
            File file = new File(dir, msg.name);

            Observable.defer(() ->
                             {
                                 try {
                                     FileUtils.writeByteArrayToFile(file, msg.content);
                                     return Observable.just(true);
                                 } catch (IOException e) {
                                     return Observable.error(e);
                                 }
                             })
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(aBoolean -> Toast.makeText(getActivity(), "File " + msg.name + " successfully written.", Toast.LENGTH_LONG).show());
        }
    }

    @OnClick(R.id.btnChooseFile)
    public void onChooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, 1337);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1337 && resultCode == Activity.RESULT_OK && data != null) {
            File f = new File(data.getData().getPath());
            Observable.defer(() ->
                             {
                                 try {
                                     byte[] content = FileUtils.readFileToByteArray(f);
                                     FileMessage msg = new FileMessage();
                                     msg.name = f.getName();
                                     msg.content = content;
                                     return Observable.just(msg);
                                 } catch (IOException e) {
                                     return Observable.error(e);
                                 }
                             })
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(this::send);
        }
    }

    protected abstract void send(FileMessage msg);

    @Override
    protected String getServiceName() {
        return Constants.SERVICE_FILES;
    }

    @Override
    protected int getDefaultServicePort() {
        return Constants.SERVICE_FILES_DEFAULT_PORT;
    }
}
