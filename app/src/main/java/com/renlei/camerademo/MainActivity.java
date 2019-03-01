package com.renlei.camerademo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private SurfaceView mViewSurface;
    private CameraUtil mCameraUtil;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private Camera mCamera;
    private int mCameraId = 0;
    private int mWidth = 1920;
    private int mHeight = 1080;
    private  SurfaceHolder mHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mViewSurface = (SurfaceView) findViewById(R.id.view_surface);

        //检查权限和硬件
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        } else {

            if (!checkCameraHardware(this)) {
                Log.i(TAG,"没有检测到相机硬件");
            } else {

                mHolder = mViewSurface.getHolder();

                mHolder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {
                        mCamera = Camera.open(mCameraId);
                        mCameraUtil = new CameraUtil(mCamera, mCameraId);
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                        mCameraUtil
                                .initCamera(mWidth,
                                        mHeight,
                                        MainActivity.this);
                        try {
                            mCamera.setPreviewDisplay(mHolder);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                        mCameraUtil.stopPreview();
                    }
                });
            }

        }
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

}
