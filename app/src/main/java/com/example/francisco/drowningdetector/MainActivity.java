package com.example.francisco.drowningdetector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class MainActivity extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private final static UUID PEBBLE_APP_UUID = UUID.fromString("55dcd496-71a9-4f14-83ae-9966d4dd19e4");

    public static final String TAG = "MainActivity";
    public static final String SERVICE_RECEIVER_TAG = "Receiver";
    public static final int SELECT_VIDEO = 1;

    private PebbleDictionary data;
    static Mat imag = null;
    static Mat orgin = null;
    static Mat kalman = null;
    public static Tracker tracker;

    //this static line is for include directly the opencv library and not use the open cv manager
    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    private JavaCameraView mOpenCvCameraView;


    //this is for using the open cv manager app for android
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        PebbleKit.registerPebbleConnectedReceiver(getApplicationContext(), new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                PebbleKit.startAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
            }
        });

        PebbleKit.registerPebbleDisconnectedReceiver(getApplicationContext(), new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Pebble Disconnect");
            }

        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.tutorial1_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mOpenCvCameraView != null)
//            mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mOpenCvCameraView != null)
//            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mOpenCvCameraView.enableView();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String toastMesage = new String();
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableView();
        Toast toast = Toast.makeText(this, toastMesage, Toast.LENGTH_LONG);
        toast.show();

        return true;
    }

    public void AlertPebble(){
        if(PebbleKit.isWatchConnected(getApplicationContext())){
            data = new PebbleDictionary();
            data.addUint8(0, (byte) 42);
            PebbleKit.sendDataToPebble(getApplicationContext(), PEBBLE_APP_UUID, data);

            PebbleKit.registerReceivedAckHandler(getApplicationContext(), new PebbleKit.PebbleAckReceiver(PEBBLE_APP_UUID) {

                @Override
                public void receiveAck(Context context, int transactionId) {
                    Log.i(getLocalClassName(), "Received ack for transaction " + transactionId);
                }

            });

            PebbleKit.registerReceivedNackHandler(getApplicationContext(), new PebbleKit.PebbleNackReceiver(PEBBLE_APP_UUID) {

                @Override
                public void receiveNack(Context context, int transactionId) {
                    if(data!= null){
                        PebbleKit.sendDataToPebble(getApplicationContext(), PEBBLE_APP_UUID, data);
                    } else {
                        data = new PebbleDictionary();
                        data.addUint8(0, (byte) 42);
                    }
                }

            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }


    private void showVideoFrames() {

    }


    private void resetServiceReceiver() {

    }

}