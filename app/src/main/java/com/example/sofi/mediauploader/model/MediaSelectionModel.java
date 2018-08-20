package com.example.sofi.mediauploader.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.sofi.mediauploader.Utils;
import com.example.sofi.mediauploader.data.Media;
import com.example.sofi.mediauploader.presenter.MediaSelectionPresenter;
import com.example.sofi.mediauploader.thread.WorkerThread;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import droidninja.filepicker.FilePickerConst;

public class MediaSelectionModel extends AppCompatActivity {

    private MediaSelectionPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mPresenter = new MediaSelectionPresenter();
    }

    public void initMediasFromStorage(Intent data, ArrayList<String> filePaths, Context context) {
        filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
        Media m;
        ArrayList<Media> media = new ArrayList<>();

        for (String path : filePaths) {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 10; i++) {
                Runnable worker = new WorkerThread(path, media);
                executor.execute(worker);
            }
            executor.shutdown();

        }
        mPresenter = new MediaSelectionPresenter();
        mPresenter.updateView(media);

    }
}
