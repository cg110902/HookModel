#ifndef __JNI_LOG_UTILS_HEADER___
#  define __JNI_LOG_UTILS_HEADER___
#include <jni.h>

#define jni_android_util_log_d(env, tag, fmt, ...)      jni_android_util_log(env, "d", tag, fmt, __VA_ARGS__)
#define jni_android_util_log_i(env, tag, fmt, ...)      jni_android_util_log(env, "i", tag, fmt, __VA_ARGS__)
#define jni_android_util_log_e(env, tag, fmt, ...)      jni_android_util_log(env, "e", tag, fmt, __VA_ARGS__)

void jni_android_util_log(JNIEnv *const env, const char* szFunction, const char* szTag ,const char *const format, ...)
{
    const jclass androidUtilLogClass = env->FindClass("android/util/Log");
    if (!androidUtilLogClass) {
        return;
    }
    const jmethodID logDotIMethodId = env->GetStaticMethodID(androidUtilLogClass, szFunction, "(Ljava/lang/String;Ljava/lang/String;)I");
    if (!logDotIMethodId) { if (androidUtilLogClass) env->DeleteLocalRef(androidUtilLogClass); return; }

    const jstring javaTag = env->NewStringUTF(szTag);

    static const int DEFAULT_LINE_SIZE = 128;
    char fixedSizeCString[DEFAULT_LINE_SIZE];
    va_list argList;
    va_start(argList, format);
    const int size = vsnprintf(fixedSizeCString, DEFAULT_LINE_SIZE, format, argList) + 1;
    va_end(argList);

    jstring javaString;
    if (size <= DEFAULT_LINE_SIZE)
    {
        javaString = env->NewStringUTF(fixedSizeCString);
    } else
    {
        va_start(argList, format);
        char variableSizeCString[size];
        vsnprintf(variableSizeCString, size, format, argList);
        va_end(argList);
        javaString = env->NewStringUTF(variableSizeCString);
    }

    env->CallStaticIntMethod(androidUtilLogClass, logDotIMethodId, javaTag, javaString);
    if (javaString) env->DeleteLocalRef(javaString);
    if (javaTag) env->DeleteLocalRef(javaTag);
    if (androidUtilLogClass) env->DeleteLocalRef(androidUtilLogClass);
}
#endif /* __JNI_LOG_UTILS_HEADER___ */