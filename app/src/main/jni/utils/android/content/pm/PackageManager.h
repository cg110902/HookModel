#ifndef __android_content_pm_PackageManager__
#define __android_content_pm_PackageManager__

#include <jni.h>
#include "ApplicationInfo.h"
#include "../../jni/ObjectBase.h"

namespace android
{
    namespace content
    {
        namespace pm
        {
            class PackageManager : public android::jni::ObjectBase
            {
                public:
                    PackageManager(jobject obj):ObjectBase(obj){  }

                    ApplicationInfo getApplicationInfo(std::string s, int i)
                    {
                        JNIEnv* env = android::jni::JniHelper::GetJniEnv();
                        return  ApplicationInfo(env->CallObjectMethod(m_object, env->GetMethodID(env->FindClass("android/content/pm/PackageManager"), "getApplicationInfo", "(Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;"), env->NewStringUTF(s.c_str()), i));
                    }
            };
        }
    }
}
#endif // __android_content_pm_PackageManager__