#include <jni.h>
#include "com_duckmedia_ndksample_MyActivity.h"
#include <opencv2/core/core.hpp>

JNIEXPORT jstring Java_com_duckmedia_ndksample_MyActivity_hello
  (JNIEnv * env, jobject obj){
    return (*env)->NewStringUTF(env, "Hello from ASDF");
  }