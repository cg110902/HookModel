#ifndef __android_content_pm_PackageItemInfo__
#define __android_content_pm_PackageItemInfo__

#include "PackageManager.h"
#include "../../jni/ObjectBase.h"

namespace android
{
    namespace content
    {
        namespace pm
        {
            class PackageItemInfo : public android::jni::ObjectBase
            {
                public:
                    PackageItemInfo(jobject obj):ObjectBase(obj){ }

                    std::string loadLabel(jobject packageManager)
                    {
                        JNIEnv* env = android::jni::helper::GetJniEnv();
                        return env->GetStringUTFChars((jstring)env->CallObjectMethod(m_object, env->GetMethodID(env->FindClass("android/content/pm/PackageItemInfo"), "loadLabel", "(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;"), packageManager) , 0);
                    }
            };
        }
    }
}

#endif // __android_content_pm_PackageItemInfo__