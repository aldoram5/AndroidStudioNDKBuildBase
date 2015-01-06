package com.duckmedia.ndksample.faceRecognizer;
import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.contrib.*;
import org.opencv.core.Mat;
import org.opencv.utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbmobile2 on 1/5/15.
 */


public class FaceRecognizerWrapper extends FaceRecognizer {
    private static FaceRecognizerWrapper instance = null;
    private FaceRecognizer recognizer;
    private int minConfidenceLevel;

    private FaceRecognizerWrapper() {
        super(n_createLBPHFaceRecognizer());
        recognizer = null;
        minConfidenceLevel = 120;
    }

    public static FaceRecognizerWrapper getInstance() {
        if (instance == null) {
            instance = new FaceRecognizerWrapper();
        }
        return instance;
    }

    public void save() {
        super.save("Test.xml");

    }

    public boolean predict(Bitmap toCheck){
        Mat mat = new Mat();
        Utils.bitmapToMat(toCheck, mat);
        int [] label = new int[1];
        double [] confidence = new double[1];
        super.predict(mat,label,confidence);
        return label[0] == 1;

    }

    public void load(){

        super.load("Test.xml");

    }

    public void train(Bitmap [] photos){

        List<Mat> mats = new ArrayList<>();

        for (int i = 0; i < photos.length; i++) {
            Mat mat = new Mat();
            Utils.bitmapToMat(photos[i],mat);
            mats.add(mat);
        }
        Mat label = new Mat();
        label.put(0,0,1);
        super.train(mats,label);
    }

    public void update(Bitmap [] photos){

        List<Mat> mats = new ArrayList<>();

        for (int i = 0; i < photos.length; i++) {
            Mat mat = new Mat();
            Utils.bitmapToMat(photos[i],mat);
            mats.add(mat);
        }
        Mat label = new Mat();
        label.put(0,0,1);
        super.update(mats, label);
    }

    //JNI Required Wrapper

    private static native long n_createLBPHFaceRecognizer();

}
