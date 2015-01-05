package com.duckmedia.ndksample.faceRecognizer;
import org.opencv.contrib.*;
/**
 * Created by gbmobile2 on 1/5/15.
 */
public class FaceRecognizerWrapper extends FaceRecognizer {
    private static FaceRecognizerWrapper instance;
    private FaceRecognizer recognizer;
    private int minConfidenceLevel;

    private FaceRecognizerWrapper() {
        super(n_createLBPHFaceRecognizer());
        recognizer = null;
        minConfidenceLevel = 120;
    }

    public static FaceRecognizerWrapper getInstance() {
        return instance;
    }

    public void save() {

    }

    public void load(){


    }

    public void train(){

    }

    //JNI Required Wrapper

    private static native long n_createLBPHFaceRecognizer();

}
