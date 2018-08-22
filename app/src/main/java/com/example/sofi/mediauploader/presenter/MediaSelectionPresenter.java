package com.example.sofi.mediauploader.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.sofi.mediauploader.R;
import com.example.sofi.mediauploader.data.Media;
import com.example.sofi.mediauploader.interfaces.MediaSelectionIface;
import com.example.sofi.mediauploader.model.MediaSelectionModel;
import com.example.sofi.mediauploader.view.MediaSelectionActivity;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;

public class MediaSelectionPresenter implements MediaSelectionIface.IPresenter {

    private MediaSelectionIface.IView mView;
    private MediaSelectionIface.IModel mModel;
    private ArrayList<String> mFilePaths;
    private Context context;

    /**
     * Overloaded constructor
     * initializes model and view layer objects.
     * @param context
     */
    public MediaSelectionPresenter(Context context) {
        this.context = context;
        mModel = new MediaSelectionModel(this);
        mView = (MediaSelectionActivity) context;
    }

    /**
     * Opens the picker, sets maximum count of selection
     * @param filePaths of selected photos
     */
    public void selectPhoto(ArrayList<String> filePaths) {
        this.mFilePaths = filePaths;
        FilePickerBuilder.getInstance().setMaxCount(5)
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto((Activity) context);
    }

    /**
     * Tells model layer to upload files from storage
     * @param data
     */
    public void initMedias(Intent data) {
        mModel.initMediasFromStorage(data, mFilePaths, context);
    }

    /**
     * returns context
     * @return
     */
    public Context getContext() {
        return context;
    }

    /**
     * Tells view layer that it should update the screan view
     * @param media
     */
    public void updateView(ArrayList<Media> media) {
        mView.updateView(media);
    }
}
