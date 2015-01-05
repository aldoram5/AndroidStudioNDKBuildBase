
#include "com_duckmedia_ndksample_faceRecognizer_FaceRecognizerWrapper.h"
#include <opencv2/core/core.hpp>

#include "opencv2/contrib/contrib.hpp"
#include <opencv2/contrib/contrib.hpp>

using namespace cv;
#ifdef __cplusplus
extern "C" {
#endif
    

    JNIEXPORT jlong JNICALL Java_com_duckmedia_ndksample_faceRecognizer_FaceRecognizerWrapper_n_1createLBPHFaceRecognizer
    (JNIEnv * env, jclass jc) {
    cv::Ptr<FaceRecognizer> model = cv::createLBPHFaceRecognizer();
    model.addref();
    return (jlong)model.obj;
}
    
    
#ifdef __cplusplus
}
#endif