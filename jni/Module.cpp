#include "Module.h"
#include <iostream>
#include <unistd.h> // To use usleep()

#define DEBUG_STREAM
#ifdef DEBUG_STREAM
static Streamer * streamerInstance = 0;
void setStreamer(Streamer* streamer) {
  streamerInstance = streamer;
}
Streamer& getStreamer() {
  return *streamerInstance;
}
#endif

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

	std::vector<float>* readVec;
	readVec = readSeqPort(false);
	if (readVec != NULL && !readVec->empty()) {
		std::vector<float> read;
		readVec->swap(read);
		writeSeqPort(read);
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



void PictureTransformModule::androidWriteSeqPort(std::vector<float> in) {
	mReadSeqBuf.push_back(in);
#ifdef DEBUG_STREAM
//	getStreamer() << "androidWriteSeqPort in.size=" << in.size() << " mReadSeqBuf.size=" << mReadSeqBuf.size() << "\n";
	getStreamer() << "androidWriteSeqPort";
	for (int i=0; i<in.size(); ++i)
		getStreamer() << in[i];
	getStreamer() << "\n";
#endif
}

AIMandroidReadSeqPort_t PictureTransformModule::androidReadSeqPort() {
	AIMandroidReadSeqPort_t ret;

#ifdef DEBUG_STREAM
//	getStreamer() << "androidReadSeqPort mWriteSeqBuf.size=" << mWriteSeqBuf.size() << "\n";
//	if (!mWriteSeqBuf.empty())
//		getStreamer() << "mWriteSeqBuf.back().size()=" << mWriteSeqBuf.back().size();
//	getStreamer() << "\n";
#endif

	if (mWriteSeqBuf.empty()) {
		ret.success = false;
		return ret;
	}
	mWriteSeqBuf.back().swap(ret.val);
	ret.success = true;
	mWriteSeqBuf.pop_back();
	return ret;
}

void PictureTransformModule::writeSeqPort(std::vector<float> &out) {
	//mWriteBuf.push_back(out); // This way out should not be changed by the user anymore..
	mWriteSeqBuf.push_back(std::vector<float>(out)); // This way out should not be changed by the user anymore..
}

std::vector<float>* PictureTransformModule::readSeqPort(bool blocking) {
	if (mReadSeqBuf.empty())
		return NULL;
	mReadSeqBuf.back().swap(mReadSeqVal);
	mReadSeqBuf.pop_back();
	return &mReadSeqVal;
}
