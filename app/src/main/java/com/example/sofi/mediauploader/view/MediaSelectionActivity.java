package com.example.sofi.mediauploader.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sofi.mediauploader.R;
import com.example.sofi.mediauploader.adapter.MediaListAdapter;
import com.example.sofi.mediauploader.data.Media;
import com.example.sofi.mediauploader.interfaces.MediaSelectionIface;
import com.example.sofi.mediauploader.presenter.MediaSelectionPresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerConst;

public class MediaSelectionActivity extends AppCompatActivity implements MediaSelectionIface.IView {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.select_photo_btn)
    Button mSelectPhotoBtn;
    @BindView(R.id.upload_photo_btn)
    Button mUploadBtn;

    ArrayList<String> filePaths = new ArrayList<>();
    MediaSelectionIface.IPresenter mPresenter;

    StorageReference mStorage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new MediaSelectionPresenter(this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        checkStoragePermission();
        mStorage = FirebaseStorage.getInstance().getReference();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkStoragePermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @OnClick(R.id.select_photo_btn)
    void selectPhoto() {
        mPresenter.selectPhoto(filePaths);
    }

    @OnClick(R.id.upload_photo_btn)
    void uploadPhoto() {
        Uri uri = null;
        for (int i = 0; i < filePaths.size(); i++) {
            uri = Uri.parse("file://" + filePaths.get(i));
            StorageReference fileToUpload = mStorage.child("Images").child(uri.getLastPathSegment());
            fileToUpload.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> Toast.makeText(MediaSelectionActivity.this, R.string.success_message, Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MediaSelectionActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
                    mPresenter.initMedias(data);
                }
        }
    }

    /**
     * updates the view: shows the list of media on screan and enables update button
     * @param media is a list which should be shown on the screen
     */
    public void updateView(ArrayList<Media> media) {
        rv.setAdapter(new MediaListAdapter(this, media));
        mUploadBtn.setVisibility(View.VISIBLE);
        mSelectPhotoBtn.setVisibility(View.GONE);
    }

}
