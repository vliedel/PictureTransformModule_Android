/* swig -c++ -java -package org.dobots.picturetransformmodule -outdir src/org/dobots/picturetransformmodule -o jni/Module_wrap.cpp jni/Module.i
-outdir		location of the .java files
-o			location of the _wrap.cpp files

module name has to be equal to the LOCAL_MODULE in the Android.mk file
ndk-build clean && ndk-build -j
*/

%module AIM

/* Declerations */
%{
#include "Module.h"
%}

/* What to export */
%include "Module.h"
