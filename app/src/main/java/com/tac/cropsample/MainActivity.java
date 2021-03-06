package com.tac.cropsample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tac.cropmodule.SCropImageView;
import com.tac.cropmodule.TransformationEngine;
import com.tac.cropsample.tools.FileUtil;
import com.tac.cropsample.tools.ImageRetriever;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SCropImageView mImageView;
    private Button mCrop;
    private ImageRetriever mMediaRetriver;
    private ImageLoader mImageLoader;
    private String[] mArray = new String[]{
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img1.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img2.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img3.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img4.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img5.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img6.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img7.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img8.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img9.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img10.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img11.jpg")).toString(),
            Uri.fromFile(new File(FileUtil.getCacheDir() + "/img12.jpg")).toString(),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaRetriver = new ImageRetriever(this, null);

        mImageLoader = ImageLoader.getInstance();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();

        mImageLoader.init(config);

//        mImageView = (CropImageView) findViewById(R.id.img);
        mImageView = (SCropImageView) findViewById(R.id.img1);
        mCrop = (Button) findViewById(R.id.btn_do);
//        mCrop.setEnabled(false);
        mCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File tmpFile = new File(FileUtil.getCacheDir(), String.valueOf(System.currentTimeMillis()));
                mImageView.cropInBackgroung(tmpFile);
            }
        });

        Button v = (Button) findViewById(R.id.btn_corners);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setupAutoPins();
            }
        });

        findViewById(R.id.btn_bw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bw();
            }
        });

        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaRetriver.getImageFromCamera();
            }
        });
        findViewById(R.id.galery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaRetriver.getImageFromGallery();
            }
        });
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(new ImageListAdapter(this, mArray));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mImageLoader.loadImage(mArray[position], new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        super.onLoadingComplete(imageUri, view, loadedImage);
//                        mImageView.setBitmap(loadedImage);
//                    }
//                });
                mImageView.loadUri(mArray[position], false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mMediaRetriver.isMineRequestCode(requestCode)) {
            MediaAttachment ma = mMediaRetriver.processResultImage(requestCode, resultCode, data);
            mImageView.loadUri(ma.getFile().getPath(), false);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

//    private void crop() {
//        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
//        //get original image Mat;
//        Mat imageMat = new Mat();
//        Utils.bitmapToMat(bitmap, imageMat);
//
//        Bitmap output = TransformationEngine.get().initArea(mImageView.pin1().point(), mImageView.pin2().point(), mImageView.pin3().point(), mImageView.pin4().point())
//                .transform(imageMat);
//
//        mImageView.setImageBitmap(output);
//    }

    private void bw() {

        //Newer mind just for compile porposes
        Bitmap bitmap = null;
        mImageView.setPinsListener(new SCropImageView.PinsSettingListener() {
            @Override
            public void onPinsSettedUp() {

            }
        });
        //get original image Mat;
        Bitmap output = TransformationEngine.processToBW(bitmap);

        mImageView.loadUri(output.toString(), true);
    }


}
