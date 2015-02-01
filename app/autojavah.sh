#!/bin/sh
export ProjectPath=$(cd "../$(dirname "$1")"; pwd)
export TargetClassName="com.sollyu.xposed.hook.model.worker.HookModelAppListWorker"

export ClassPath="${ProjectPath}/app/build/intermediates/classes/debug"
export TargetPath="${ProjectPath}/app/src/main/jni"

echo javah -classpath "${ClassPath}" -d "${TargetPath}" "${TargetClassName}"
javah -classpath "${ClassPath}" -d "${TargetPath}" "${TargetClassName}"