package com.example.sofi.mediauploader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sofi.mediauploader.adapter.MediaListAdapter;
import com.example.sofi.mediauploader.data.Media;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.select_photo_btn)
    Button mSelectPhotoBtn;
    @BindView(R.id.upload_photo_btn)
    Button mUploadBtn;

    ArrayList<String> filePaths = new ArrayList<>();

    StorageReference mStorage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
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
        FilePickerBuilder.getInstance().setMaxCount(5)
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(MainActivity.this);
    }

    @OnClick(R.id.upload_photo_btn)
    void uploadPhoto() {
        Uri uri = null;
        for (int i = 0; i < filePaths.size(); i++) {
            uri = Uri.parse("file://" + filePaths.get(i));
            StorageReference fileToUpload = mStorage.child("Images").child(uri.getLastPathSegment());
            fileToUpload.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> Toast.makeText(MainActivity.this, "onSuccess", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Something went wrong, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
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
                    getFilesFromStorage(data);
                }
        }
    }

    private void getFilesFromStorage(Intent data) {
        filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
        Media m;
        ArrayList<Media> media = new ArrayList<>();

        try {
            for (String path : filePaths) {
                m = new Media();
                m.setName(path.substring(path.lastIndexOf("/") + 1));

                Uri uri = Uri.fromFile(new File(path));
                m.setUri(uri);
                media.add(m);
                Utils.resizeAndCompressImageBeforeSend(this, path, path.substring(path.lastIndexOf("/") + 1));
            }
            updateView(media);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateView(ArrayList<Media> media) {
        rv.setAdapter(new MediaListAdapter(this, media));
        mUploadBtn.setVisibility(View.VISIBLE);
        mSelectPhotoBtn.setVisibility(View.GONE);
    }
}
