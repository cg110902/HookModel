#ifndef __JNI_TOOLS_HELPER_HEADER___
#  define __JNI_TOOLS_HELPER_HEADER___
#include <jni.h>

namespace UTILS
{
    jstring GetPackageName(jobject context)
    {
        jclass cls_Context           = env->FindClass("android/content/Context");
        jclass PackageManager        = env->FindClass("android/content/pm/PackageManager");
        jclass PackageItemInfo       = env->FindClass("android/content/pm/PackageItemInfo");

        jmethodID getPackageName     = env->GetMethodID(cls_Context,    "getPackageName",     "()Ljava/lang/String;");
        jmethodID getPackageManager  = env->GetMethodID(cls_Context,    "getPackageManager",  "()Landroid/content/pm/PackageManager;");
        jmethodID getApplicationInfo = env->GetMethodID(PackageManager, "getApplicationInfo", "(Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;");
        jmethodID loadLabel          = env->GetMethodID(PackageItemInfo, "loadLabel",         "(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;");

        jstring str_packageName      = (jstring)env->CallObjectMethod(context, getPackageName);
        return str_packageName;
    }



    jstring GetApplicationMetaData(JNIEnv *env, jobject context, jstring key)
    {
        jclass cls_Context           = env->FindClass("android/content/Context");
        jclass PackageManager        = env->FindClass("android/content/pm/PackageManager");
        jclass PackageItemInfo       = env->FindClass("android/content/pm/PackageItemInfo");
        jclass Bundle                = env->FindClass("android/os/Bundle");

        jmethodID getPackageName     = env->GetMethodID(cls_Context    , "getPackageName"     , "()Ljava/lang/String;");
        jmethodID getPackageManager  = env->GetMethodID(cls_Context    , "getPackageManager"  , "()Landroid/content/pm/PackageManager;");
        jmethodID getApplicationInfo = env->GetMethodID(PackageManager , "getApplicationInfo" , "(Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;");
        jmethodID loadLabel          = env->GetMethodID(PackageItemInfo, "loadLabel"          , "(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;");
        jmethodID getString          = env->GetMethodID(Bundle         , "getString"          , "(Ljava/lang/String;)Ljava/lang/String;");

        jfieldID imp_metaData        = env->GetFieldID(PackageItemInfo, "metaData", "Landroid/os/Bundle;");

        jstring str_packageName      = (jstring)env->CallObjectMethod(context, getPackageName);

        jobject pm = env->CallObjectMethod(context, getPackageManager);
        jobject in = env->CallObjectMethod(pm, getApplicationInfo, str_packageName, 0x80);

        jobject metaData    = env->GetObjectField(in, imp_metaData);
        jstring metaDataKey = (jstring)env->CallObjectMethod(metaData, getString, key);

        return metaDataKey;
    }
}

#endif // __JNI_TOOLS_HELPER_HEADER___