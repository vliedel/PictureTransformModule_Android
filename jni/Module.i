/* swig -c++ -java -package org.dobots.picturetransformmodule -outdir src/org/dobots/picturetransformmodule -o jni/Module_wrap.cpp jni/Module.i */

%module AIM

/* Declerations */
%{
#include "Module.h"
%}

/* What to export */
%include "Module.h"
