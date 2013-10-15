# File: Android.mk
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

# Name of the lib
LOCAL_MODULE    := PictureTransformModule
LOCAL_SRC_FILES := Module_wrap.cpp Module.cpp
LOCAL_CFLAGS    := -frtti


include $(BUILD_SHARED_LIBRARY)
