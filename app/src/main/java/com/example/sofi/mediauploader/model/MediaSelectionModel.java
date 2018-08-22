package com.example.sofi.mediauploader.model;

import android.content.Context;
import android.content.Intent;

import com.example.sofi.mediauploader.data.Media;
import com.example.sofi.mediauploader.interfaces.MediaSelectionIface;
import com.example.sofi.mediauploader.presenter.MediaSelectionPresenter;
import com.example.sofi.mediauploader.thread.WorkerThread;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import droidninja.filepicker.FilePickerConst;

public class MediaSelectionModel implements MediaSelectionIface.IModel {

    private MediaSelectionPresenter mPresenter;

    public MediaSelectionModel(MediaSelectionPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**
     * Uploads content and name of media
     * and tells presenter that work is done
     * @param data
     * @param filePaths
     * @param context
     */
    public void initMediasFromStorage(Intent data, ArrayList<String> filePaths, Context context) {
        filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
        Media m;
        ArrayList<Media> media = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (String path : filePaths) {
                Runnable worker = new WorkerThread(path, media);
                executor.execute(worker);

        }executor.shutdown();
        mPresenter.updateView(media);
    }
}
