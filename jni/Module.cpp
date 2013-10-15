#include "Module.h"
#include <iostream>
#include <unistd.h> // To use usleep()

using namespace cimg_library;

PictureTransformModule::PictureTransformModule() {
	mIntImg.resize(10, 10, 1, 1); // Size 10*10*1*1
	mIndex = 0;
}

void PictureTransformModule::Tick() {
	int* readInt = readPort(false);
	if (readInt != NULL) {
		std::cout << "Read: " << *readInt << std::endl;
		if (mIndex < 10*10) {
			mIntImg(mIndex) = *readInt;
			++mIndex;
		}

		//writePort(*readInt);
		writePort(mIntImg.mean());
	}
	usleep(1000*1000);
}

void PictureTransformModule::androidWritePort(int in) {
	mReadBuf.push_back(in);
}

//int* PictureTransformModule::androidReadPort() {
//	if (mWriteBuf.empty())
//		return NULL;
//	mAndroidReadVal = mWriteBuf.back();
//	mWriteBuf.pop_back();
//	return &mAndroidReadVal;
//}

//bool PictureTransformModule::androidReadPort(int &out) {
//	if (mWriteBuf.empty())
//		return false;
//	out = mWriteBuf.back();
//	mWriteBuf.pop_back();
//	return true;
//}

AIMandroidReadPort_t PictureTransformModule::androidReadPort() {
	AIMandroidReadPort_t ret;
	if (mWriteBuf.empty()) {
		ret.success = false;
		return ret;
	}
	ret.val = mWriteBuf.back();
	ret.success = true;
	mWriteBuf.pop_back();
	return ret;
}

void PictureTransformModule::writePort(int out) {
	mWriteBuf.push_back(out);
}

int* PictureTransformModule::readPort(bool blocking) {
	if (mReadBuf.empty())
		return NULL;
	mReadVal = mReadBuf.back();
	mReadBuf.pop_back();
	return &mReadVal;
}
