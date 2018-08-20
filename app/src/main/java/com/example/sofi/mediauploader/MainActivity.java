package com.example.sofi.mediauploader;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sofi.mediauploader.adapter.MediaListAdapter;
import com.example.sofi.mediauploader.data.Media;

import java.io.File;
import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<String> filePaths = new ArrayList<>();
    Button mSelectPhotoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        mSelectPhotoBtn = (Button) findViewById(R.id.select_photo_btn);
        mSelectPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePaths.clear();

                if (mSelectPhotoBtn.getText().equals("Upload")) {
//                            StorageReference fileToUpload = mStorage.child("Images").child(single.getLastPathSegment());
//
//                            fileToUpload.putFile(single).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                    Toast.makeText(MainActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
//
//                                }
//                            });
                } else {
                    FilePickerBuilder.getInstance().setMaxCount(5)
                            .setSelectedFiles(filePaths)
                            .setActivityTheme(R.style.AppTheme)
                            .pickPhoto(MainActivity.this);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    getFilesFromStorage(data);
                    mSelectPhotoBtn.setText("Upload");
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

                m.setUri(Uri.fromFile(new File(path)));
                media.add(m);

            }

            rv.setAdapter(new MediaListAdapter(this, media));
            Toast.makeText(MainActivity.this, "Total selected items = " + String.valueOf(media.size()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
