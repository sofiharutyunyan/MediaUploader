package com.example.sofi.mediauploader.interfaces;

import android.content.Context;
import android.content.Intent;
import com.example.sofi.mediauploader.data.Media;
import java.util.ArrayList;

/**
 * Created by sofi on 8/22/18.
 */

/**
 * Helper interface for Design Pattern {MVP}
 */
public interface MediaSelectionIface {

    interface IView {

        void updateView(ArrayList<Media> media);
    }

    interface IPresenter {

        void selectPhoto(ArrayList<String> filePaths);

        void initMedias(Intent data);

        Context getContext();
    }

    interface IModel {

        void initMediasFromStorage(Intent data, ArrayList<String> filePaths, Context context);
    }
}
