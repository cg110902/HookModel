#ifndef __android_jni_Jni__
#define __android_jni_Jni__

#include <jni.h>

static JNIEnv* android_jni_JniHelper_javaEnv;

namespace android
{
    namespace jni
    {
        class helper
        {
            public:
                static JNIEnv* GetJniEnv() { return android_jni_JniHelper_javaEnv; }
                static void Init(JNIEnv* Env){ android_jni_JniHelper_javaEnv = Env; }
                static std::string JstringToString( jstring     str ) { return GetJniEnv()->GetStringUTFChars(str, 0); }
                static jstring     StringToJstring( std::string str ) { return GetJniEnv()->NewStringUTF(str.c_str()); }

            public:
                template<typename RETURN_TYPE>
                static RETURN_TYPE CallObjectMethod(jobject target, std::string className, std::string methodName, std::string resultName) { return (RETURN_TYPE)GetJniEnv()->CallObjectMethod(target, GetJniEnv()->GetMethodID(GetJniEnv()->FindClass(className.c_str()), methodName.c_str(), resultName.c_str())); }

                template<typename RETURN_TYPE, typename ARG1>
                static RETURN_TYPE CallObjectMethod(jobject target, std::string className, std::string methodName, std::string resultName, ARG1 arg1) { return (RETURN_TYPE)GetJniEnv()->CallObjectMethod(target, GetJniEnv()->GetMethodID(GetJniEnv()->FindClass(className.c_str()), methodName.c_str(), resultName.c_str()), arg1); }

                template<typename RETURN_TYPE, typename ARG1, typename ARG2>
                static RETURN_TYPE CallObjectMethod(jobject target, std::string className, std::string methodName, std::string resultName, ARG1 arg1, ARG2 arg2) { return (RETURN_TYPE)GetJniEnv()->CallObjectMethod(target, GetJniEnv()->GetMethodID(GetJniEnv()->FindClass(className.c_str()), methodName.c_str(), resultName.c_str()), arg1, arg2); }

                template<typename RETURN_TYPE, typename ARG1, typename ARG2, typename ARG3>
                static RETURN_TYPE CallObjectMethod(jobject target, std::string className, std::string methodName, std::string resultName, ARG1 arg1, ARG2 arg2, ARG3 arg3) { return (RETURN_TYPE)GetJniEnv()->CallObjectMethod(target, GetJniEnv()->GetMethodID(GetJniEnv()->FindClass(className.c_str()), methodName.c_str(), resultName.c_str()), arg1, arg2, arg3); }

            public:
                template<typename RETURN_TYPE>
                static RETURN_TYPE GetObjectField(jobject target, std::string className, std::string methodName, std::string resultName) { return (RETURN_TYPE)GetJniEnv()->GetObjectField(target, GetJniEnv()->GetFieldID(GetJniEnv()->FindClass(className.c_str()), methodName.c_str(), resultName.c_str())); }

                template<typename RETURN_TYPE, typename ARG1>
                static RETURN_TYPE GetObjectField(jobject target, std::string className, std::string methodName, std::string resultName, ARG1 arg1) { return (RETURN_TYPE)GetJniEnv()->GetObjectField(target, GetJniEnv()->GetFieldID(GetJniEnv()->FindClass(className.c_str()), methodName.c_str(), resultName.c_str()), arg1); }

                template<typename RETURN_TYPE, typename ARG1, typename ARG2>
                static RETURN_TYPE GetObjectField(jobject target, std::string className, std::string methodName, std::string resultName, ARG1 arg1, ARG2 arg2) { return (RETURN_TYPE)GetJniEnv()->GetObjectField(target, GetJniEnv()->GetFieldID(GetJniEnv()->FindClass(className.c_str()), methodName.c_str(), resultName.c_str()), arg1, arg2); }

                template<typename RETURN_TYPE, typename ARG1, typename ARG2, typename ARG3>
                static RETURN_TYPE GetObjectField(jobject target, std::string className, std::string methodName, std::string resultName, ARG1 arg1, ARG2 arg2, ARG3 arg3) { return (RETURN_TYPE)GetJniEnv()->GetObjectField(target, GetJniEnv()->GetFieldID(GetJniEnv()->FindClass(className.c_str()), methodName.c_str(), resultName.c_str()), arg1, arg2, arg3); }

        };



    }
}

#endif //__android_jni_Jni__