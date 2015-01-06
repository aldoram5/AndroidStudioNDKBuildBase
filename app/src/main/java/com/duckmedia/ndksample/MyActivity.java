package com.duckmedia.ndksample;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MyActivity extends Activity implements View.OnClickListener {//implements CameraBridgeViewBase.CvCameraViewListener2{


    private Camera mCamera;

    // The surface view for the camera data
    private CameraPreview mView;

    private boolean theresCam = false;
    private Context context = null;
    //private PictureCallback mPicture = new PictureCallback();

    // Draw rectangles and other fancy stuff:
    private FaceOverlayView mFaceView;

    public static final String TAG = MyActivity.class.getSimpleName();

    /**
     * Sets the faces for the overlay view, so it can be updated
     * and the face overlays will be drawn again.
     */
    private Camera.FaceDetectionListener faceDetectionListener = new Camera.FaceDetectionListener() {
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            Log.d("onFaceDetection", "Number of Faces:" + faces.length);
            // Update the view now!
            mFaceView.setFaces(faces);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.d(TAG, "Setting the context");
        context = this;

        Log.d(TAG, "looking for cameras in the device");
        theresCam = checkCameraHardware(context);

        mCamera = null;
        mView = null;

        /* Create an instance of Camera
        mCamera = getCameraInstance();

        this.initUIListeners();
        if(null != mCamera){
            mCamera.setDisplayOrientation(90);
            // Create our Preview view and set it as the content of our activity.
            mView = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mView);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(theresCam) {
            this.initUIListeners();
            Log.d(TAG, "Camera found");

            // Create an instance of Camera
            mCamera = getCameraInstance();

            if(mCamera != null) {
                mCamera.setDisplayOrientation(90);

                // Create the OverlayView:
                mFaceView = new FaceOverlayView(this);

                // Create our Preview view and set it as the content of our activity.
                mView = new CameraPreview(this, mCamera, faceDetectionListener, mFaceView);
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.addView(mView);

                // Add this view
                addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            } else Log.d(TAG, "fail to get a device's camera");

        }
        else Log.d(TAG, "there aren't cameras attached to the device");
    }

    private void initUIListeners(){

        Button bc = (Button) findViewById(R.id.button_capture);
        bc.setOnClickListener(this);

    }


    /** A safe way to get an instance of the Camera object. */
    public Camera getCameraInstance(){
        releaseCameraAndPreview();

        Camera c = null;

        try {
            Log.d(TAG, "Attempt to get a Camera instance");
            int camID = MyActivity.getFrontCameraId();
            if(camID != -1)
                c = Camera.open(camID); // attempt to get a Camera instance
            else
                throw new Exception("No front camera found");
            Log.d("mainActivity", "We got a Camera instance");
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Camera is not available (in use or does not exist)");
            e.printStackTrace();
        }
        return c; // returns null if mCamera is unavailable
    }

    private void releaseCameraAndPreview() {
        Log.d(TAG, "Release Camera & preview");

        if(mView != null) mView.setCamera(null);

        Log.d(TAG, "Release Camera");
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private static int getFrontCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }

        return camId;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }
/*
    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }
*/
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_capture:
               /* System.out.println("Button pressed");
                this.releaseCamera();
                Intent intent = new Intent(this,MainTabActivity.class);
                startActivity(intent);*/
                mCamera.takePicture(myShutterCallback,null, myPictureCallback_JPG);
                break;
        }
    }





    //Custom Methods

    /** Check if this device has a mCamera */
    private boolean checkCameraHardware(Context context) {

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a mCamera
            return true;
        } else {
            // no mCamera on this device
            return false;
        }
    }

    ShutterCallback myShutterCallback = new ShutterCallback(){

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }};

    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback(){

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            Log.d(TAG, "Saving Image");
            // TODO Auto-generated method stub
            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);

            Log.d(TAG, "bitmapPicture " + bitmapPicture.toString());

            String filename = "pippo.jpg";
            File sd = Environment.getExternalStorageDirectory();
            File dest = new File(sd, filename);


            FileOutputStream out = null;
            try {
                out = new FileOutputStream(dest);
                bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored

                Log.d(TAG, "Image saved");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }};

}



/*
class PictureCallback implements Camera.PictureCallback {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            System.out.println("Error creating media file, check storage permissions");
            //Log.d(TAG, "Error creating media file, check storage permissions: " + e.getMessage());
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            //Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error accessing file");
            //Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    */
/** Create a file Uri for saving an image or video *//*

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    */
/** Create a File for saving an image or video *//*

    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}*/
