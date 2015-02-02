#ifndef __android_content_Context__
#define __android_content_Context__

#include <jni.h>
#include <string>
#include "pm/PackageManager.h"
#include "../jni/ObjectBase.h"

namespace android
{
    namespace content
    {
        class Context : public android::jni::ObjectBase
        {
            public:
                Context(jobject obj):ObjectBase(obj){  }

                android::content::pm::PackageManager getPackageManager()
                {
                    JNIEnv* env = android::jni::JniHelper::GetJniEnv();

                    jclass    ContextClass      = env->FindClass("android/content/Context");
                    jmethodID getPackageManager = env->GetMethodID(ContextClass, "getPackageManager", "()Landroid/content/pm/PackageManager;");
                    return android::content::pm::PackageManager(env->CallObjectMethod(m_object, getPackageManager));
                }

                std::string getPackageName()
                {
                    JNIEnv* env = android::jni::JniHelper::GetJniEnv();
                    return env->GetStringUTFChars((jstring)env->CallObjectMethod(m_object, env->GetMethodID(env->FindClass("android/content/Context"), "getPackageName", "()Ljava/lang/String;")), 0);
                }
        };
    }
}

#endif  // __android_content_Context__