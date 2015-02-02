#ifndef __android_jni_Jni__
#define __android_jni_Jni__

#include <jni.h>

static JNIEnv* android_jni_JniHelper_javaEnv;

namespace android
{
    namespace jni
    {
        class JniHelper
        {
            public:
                static JNIEnv* GetJniEnv() { return android_jni_JniHelper_javaEnv; }
                static void Init(JNIEnv* Env){ android_jni_JniHelper_javaEnv = Env; }
        };

    }
}

#endif //__android_jni_Jni__