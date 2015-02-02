#ifndef __android_jni_ObjectBase__
#define __android_jni_ObjectBase__

#include <jni.h>

namespace android
{
    namespace jni
    {
        class ObjectBase
        {
            protected:
                jobject m_object;
            public:
                ObjectBase(jobject obj) { m_object = obj; }
                jobject getObject() { return m_object; }
                jobject setObject(jobject obj) { m_object = obj; return getObject(); }
        };
    }
}

#endif // __android_jni_ObjectBase__