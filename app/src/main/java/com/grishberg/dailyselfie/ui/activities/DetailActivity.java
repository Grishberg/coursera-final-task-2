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
import com.grishberg.dailyselfie.data.db.dao.PictureDao;
import com.grishberg.dailyselfie.data.db.dao.PictureDaoCursor;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivPicture;
    private PictureDao pictureDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        pictureDao = new PictureDaoCursor(getApplicationContext());
        Intent intent = getIntent();
        populatePicture(intent);
    }

    private void populatePicture(Intent intent){
        long id = intent.getLongExtra(MainActivity.PHOTO_ID, -1);
        if(id > 0) {
            pictureDao.getPicture(id);
        }

    }
}
