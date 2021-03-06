package com.grishberg.dailyselfie;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.test.ApplicationTestCase;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.grishberg.dailyselfie.data.db.AppContentProvider;
import com.grishberg.dailyselfie.data.db.dao.PictureDao;
import com.grishberg.dailyselfie.data.db.dao.PictureDaoCursor;
import com.grishberg.dailyselfie.data.files.PictureManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public static final int TIMEOUT = 50;
    private static final String TAG = ApplicationTest.class.getSimpleName();
    private boolean success;
    MockContentResolver mockResolver;
    PictureDao pictureDao;
    ContextWithMockContentResolver mockContext;
    String path;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    public void setUp() {
        AppContentProvider contentProvider = new AppContentProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = AppContentProvider.AUTHORITY;
        mockResolver = new MockContentResolver();
        mockResolver.addProvider(AppContentProvider.AUTHORITY, contentProvider);
        mockContext = new ContextWithMockContentResolver(super.getContext());
        mockContext.setContentResolver(mockResolver);
        contentProvider.attachInfo(getContext(), providerInfo);
        contentProvider.onCreate();
        pictureDao = new PictureDaoCursor(getContext());
    }

    public void testStoreTask() throws Exception {

        final CountDownLatch signal = new CountDownLatch(1);
        success = false;
        PictureManager pictureManager = PictureManager.getInstance();
        Bitmap testBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        pictureManager.storePicture(getContext(), testBitmap, new PictureManager.StoreCompleteListener() {
            @Override
            public void onCompleted(String path) {
                assertNotNull(path);
                ApplicationTest.this.path = path;
                success = true;
                signal.countDown();
            }

            @Override
            public void onFail(String message) {
                Log.e(TAG, "storePicture onFail: " + message);
                assertTrue("storePicture onFail: ", true);
                signal.countDown();
            }
        });
        signal.await(TIMEOUT, TimeUnit.SECONDS);
        assertTrue("picture was not stored", success);
    }

    public void testReadTask() throws Exception {
        path = null;
        testStoreTask();

        final CountDownLatch signal = new CountDownLatch(1);
        success = false;
        PictureManager pictureManager = PictureManager.getInstance();
        pictureManager.loadPicture(path, new PictureManager.DecodeCompleteListener() {
            @Override
            public void onCompleted(Bitmap bitmap, String path) {
                Log.d(TAG, "loadPicture onCompleted: " + path);
                assertNotNull(bitmap);
                success = bitmap != null;
                signal.countDown();
            }

            @Override
            public void onFail(String message) {
                Log.e(TAG, "loadPicture onFail: " + message);
                assertTrue("loadPicture onFail: ", true);
                signal.countDown();
            }
        });
        signal.await(TIMEOUT, TimeUnit.SECONDS);
        assertTrue("picture was not loaded", success);
    }

    //----------------------------------- setup test -------------------------------------

    public static class ContextWithMockContentResolver extends RenamingDelegatingContext {
        private ContentResolver contentResolver;

        public void setContentResolver(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        public ContextWithMockContentResolver(Context targetContext) {
            super(targetContext, "test");
        }

        @Override
        public ContentResolver getContentResolver() {
            return contentResolver;
        }

        @Override
        public Context getApplicationContext() {
            return this;
        } //Added in-case my class called getApplicationContext()
    }

    @Override
    public Context getContext() {
        return mockContext;
    }
}