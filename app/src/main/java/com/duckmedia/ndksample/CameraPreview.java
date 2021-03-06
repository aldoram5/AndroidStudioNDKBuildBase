package com.duckmedia.ndksample;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    // Draw rectangles and other fancy stuff:
    private FaceOverlayView mFaceView;


    /**
     * Sets the faces for the overlay view, so it can be updated
     * and the face overlays will be drawn again.
     */
    private Camera.FaceDetectionListener faceDetectionListener;


    public CameraPreview(Context context, Camera camera, Camera.FaceDetectionListener fdl, FaceOverlayView fov ) {
        super(context);
        mCamera = camera;


        init();
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.

        this.mFaceView = fov;
        this.faceDetectionListener = new Camera.FaceDetectionListener() {
            @Override
            public void onFaceDetection(Camera.Face[] faces, Camera camera) {
                Log.d("onFaceDetection", "Number of Faces:" + faces.length);
                // Update the view now!
                mFaceView.setFaces(faces);
            }
        };
    }

    public CameraPreview(Context context, Camera mCamera, FaceOverlayView mFaceView) {
        super(context);
        this.mCamera = mCamera;
        this.mFaceView = mFaceView;
        init();
    }


    private void init(){
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }


    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        mCamera.setFaceDetectionListener(faceDetectionListener);
        mCamera.startFaceDetection();
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("ASDA", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.getHolder().removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.setFaceDetectionListener(null);
        mCamera.setErrorCallback(null);
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("ASDA", "Error starting camera preview: " + e.getMessage());
        }
    }

    public void setCamera(Camera camera){this.mCamera = camera;}
    public void setFaceDetectionListener(Camera.FaceDetectionListener fdl){this.faceDetectionListener = fdl; }
    public void setFaceOverlayView(FaceOverlayView fov){ this.mFaceView = fov;}
}