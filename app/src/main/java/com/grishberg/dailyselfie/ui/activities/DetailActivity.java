package com.grishberg.dailyselfie.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.grishberg.dailyselfie.R;
import com.grishberg.dailyselfie.common.db.DataReceiveObserver;
import com.grishberg.dailyselfie.common.db.ListResult;
import com.grishberg.dailyselfie.common.db.SingleResult;
import com.grishberg.dailyselfie.data.db.dao.PictureDao;
import com.grishberg.dailyselfie.data.db.dao.PictureDaoCursor;
import com.grishberg.dailyselfie.data.files.PictureManager;
import com.grishberg.dailyselfie.data.model.Pictures;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity
        implements DataReceiveObserver,
        PictureManager.DecodeCompleteListener {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss", Locale.US);

    private ImageView ivPicture;
    private TextView tvCreated;
    private PictureDao pictureDao;
    private SingleResult<Pictures> singleResult;
    private PictureManager pictureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        tvCreated = (TextView) findViewById(R.id.tvCreated);
        pictureDao = new PictureDaoCursor(getApplicationContext());
        pictureManager = PictureManager.getInstance();
        Intent intent = getIntent();
        populatePicture(intent);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        if (singleResult != null) {
            singleResult.release();
        }
    }

    @Override
    public void onDataReceived() {
        Log.d(TAG, "onDataReceived: ");
        Pictures item = singleResult.getItem();
        pictureManager.loadPicture(item.getPath(), this);
        tvCreated.setText(sdf.format(new Date(item.getLastupdate())));
    }

    private void populatePicture(Intent intent) {
        long id = intent.getLongExtra(MainActivity.PHOTO_ID, -1);
        Log.d(TAG, "populatePicture: id = " + id);
        if (id > 0) {
            singleResult = pictureDao.getPicture(id);
            singleResult.addDataReceiveObserver(this);
        }
    }

    @Override
    public void onCompleted(Bitmap bitmap, String path) {
        Log.d(TAG, "onCompleted: path=" + path);
        Animation fadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        ivPicture.setImageBitmap(bitmap);
        ivPicture.startAnimation(fadeAnimation);
    }

    @Override
    public void onFail(String error) {
        Log.e(TAG, "onFail: reason:" + error);
    }
}
