#ifndef __android_content_pm_ApplicationInfo__
#define __android_content_pm_ApplicationInfo__

#include <jni.h>
#include <string>
#include "PackageItemInfo.h"

namespace android
{
    namespace content
    {
        namespace pm
        {
            class ApplicationInfo : public PackageItemInfo
            {
                public:
                    ApplicationInfo(jobject obj):PackageItemInfo(obj){  }
            };
        }
    }
}

#endif // __android_content_pm_ApplicationInfo__