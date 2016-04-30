package com.grishberg.dailyselfie.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.grishberg.dailyselfie.R;
import com.grishberg.dailyselfie.common.db.DataReceiveObserver;
import com.grishberg.dailyselfie.common.db.ListResult;
import com.grishberg.dailyselfie.common.db.SingleResult;
import com.grishberg.dailyselfie.data.db.dao.PictureDao;
import com.grishberg.dailyselfie.data.db.dao.PictureDaoCursor;
import com.grishberg.dailyselfie.data.files.PictureManager;
import com.grishberg.dailyselfie.data.model.Pictures;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailActivity extends AppCompatActivity implements DataReceiveObserver {

    private ImageView ivPicture;
    private PictureDao pictureDao;
    private SingleResult<Pictures> singleResult;
    private PictureManager pictureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        pictureDao = new PictureDaoCursor(getApplicationContext());
        pictureManager = PictureManager.getInstance();
        Intent intent = getIntent();
        populatePicture(intent);
    }

    @Override
    public void onDataReceived() {

    }

    private void populatePicture(Intent intent){
        long id = intent.getLongExtra(MainActivity.PHOTO_ID, -1);
        if(id > 0) {
            singleResult = pictureDao.getPicture(id);
        }
    }
}
