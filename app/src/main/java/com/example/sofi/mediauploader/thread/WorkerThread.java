package com.example.sofi.mediauploader.thread;

import android.net.Uri;

import com.example.sofi.mediauploader.data.Media;

import java.io.File;
import java.util.ArrayList;

/**
 * WorkerThread is responsible for creating
 * media object and adding it on list
 */
public class WorkerThread implements Runnable{

    private String mPath;
    private ArrayList<Media> mMedia;

    public WorkerThread(String mPath, ArrayList<Media> mMedia) {
        this.mPath = mPath;
        this.mMedia = mMedia;
    }

    @Override
    public void run() {
        Media m = new Media();
        m.setName(mPath.substring(mPath.lastIndexOf("/") + 1));

        Uri uri = Uri.fromFile(new File(mPath));
        m.setUri(uri);
        mMedia.add(m);
    }
}
