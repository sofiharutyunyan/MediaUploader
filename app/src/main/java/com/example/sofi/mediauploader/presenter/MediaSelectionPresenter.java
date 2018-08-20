package com.example.sofi.mediauploader.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.sofi.mediauploader.R;
import com.example.sofi.mediauploader.data.Media;
import com.example.sofi.mediauploader.model.MediaSelectionModel;
import com.example.sofi.mediauploader.view.MediaSelectionActivity;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class MediaSelectionPresenter extends AppCompatActivity {

    private MediaSelectionActivity mView;
    private MediaSelectionModel mModel;
    private ArrayList<String> mFilePaths;
    private Context context;

    public MediaSelectionPresenter(Context context) {
        this.context = context;
        mModel = new MediaSelectionModel();
        mView = new MediaSelectionActivity();
    }

    public MediaSelectionPresenter() {
        mModel = new MediaSelectionModel();
        mView = new MediaSelectionActivity();
    }

    public void selectPhoto(ArrayList<String> filePaths) {
        this.mFilePaths = filePaths;
        FilePickerBuilder.getInstance().setMaxCount(5)
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto((Activity) getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    mModel.initMediasFromStorage(data, mFilePaths, context);
                }
        }
    }

    public Context getContext() {
        return context;
    }

    public void updateView(ArrayList<Media> media) {
        mView.updateView(media);
    }
}
