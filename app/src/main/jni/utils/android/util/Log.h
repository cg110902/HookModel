#ifndef __android_util_Log__
#define __android_util_Log__

#include <jni.h>
#include <string>
#include "../jni/JniHelper.h"

namespace android
{
    namespace util
    {
        class Log
        {
            public:
                static int v(std::string tag, std::string msg)   { return CallMethod(tag, msg, "v"  ); }
                static int d(std::string tag, std::string msg)   { return CallMethod(tag, msg, "d"  ); }
                static int i(std::string tag, std::string msg)   { return CallMethod(tag, msg, "i"  ); }
                static int w(std::string tag, std::string msg)   { return CallMethod(tag, msg, "w"  ); }
                static int e(std::string tag, std::string msg)   { return CallMethod(tag, msg, "e"  ); }
                static int wtf(std::string tag, std::string msg) { return CallMethod(tag, msg, "wtf"); }

            private:
                static int CallMethod(std::string tag, std::string msg, std::string szMethod)
                {
                    JNIEnv* env = android::jni::helper::GetJniEnv();
                    jstring jTag = env->NewStringUTF(tag.c_str());
                    jstring jMsg = env->NewStringUTF(msg.c_str());

                    jclass    LogClass = env->FindClass("android/util/Log");
                    jmethodID MethodId = env->GetStaticMethodID(LogClass, szMethod.c_str(), "(Ljava/lang/String;Ljava/lang/String;)I");
                    return env->CallStaticIntMethod(LogClass, MethodId, jTag, jMsg);
                }
        };
    }
}

#endif