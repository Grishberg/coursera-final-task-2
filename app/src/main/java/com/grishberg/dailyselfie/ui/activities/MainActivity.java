package com.grishberg.dailyselfie.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.grishberg.dailyselfie.App;
import com.grishberg.dailyselfie.R;
import com.grishberg.dailyselfie.common.db.DataStoreObserver;
import com.grishberg.dailyselfie.common.db.ListResult;
import com.grishberg.dailyselfie.common.interfaces.OnItemClickListener;
import com.grishberg.dailyselfie.controllers.PicturesAdapter;
import com.grishberg.dailyselfie.data.db.ListResultCursor;
import com.grishberg.dailyselfie.data.db.dao.PictureDao;
import com.grishberg.dailyselfie.data.db.dao.PictureDaoCursor;
import com.grishberg.dailyselfie.data.files.PictureManager;
import com.grishberg.dailyselfie.data.model.Pictures;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, DataStoreObserver {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String PHOTO_ID = "PHOTO_ID";

    private PictureManager pictureManager;
    private RecyclerView recyclerView;
    private PicturesAdapter adapter;
    private PictureDao pictureDao;
    private ListResult<Pictures> resultCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pictureManager = PictureManager.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.rvPictures);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        pictureDao = new PictureDaoCursor(getApplicationContext());
        resultCursor = pictureDao.getPictures();
        adapter = new PicturesAdapter(getApplicationContext(),
                resultCursor,
                pictureManager,
                this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resultCursor != null) {
            resultCursor.release();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_take_photo:
                dispatchTakePictureIntent();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void dispatchTakePictureIntent() {
        Log.d(TAG, "dispatchTakePictureIntent: ");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            pictureManager.storePicture(App.getAppContext(), imageBitmap, new PictureManager.StoreCompleteListener() {
                @Override
                public void onCompleted(String path) {
                    pictureDao.storePictureAsync(path, MainActivity.this);
                }

                @Override
                public void onFail(String path) {
                    Log.e(TAG, "onFail: " + path);
                }
            });
        }
    }

    @Override
    public void onDataStored(long id) {
        adapter.notifyDataSetChanged();
    }

    /**
     * start detail activity
     *
     * @param id
     */
    @Override
    public void onItemClicked(long id) {
        Log.d(TAG, "onItemClicked: " + id);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(PHOTO_ID, id);
        startActivity(intent);
    }
}
