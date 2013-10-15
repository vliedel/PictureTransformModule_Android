#include <vector>

// CImg configuration
#define cimg_verbosity 2
#define cimg_display 0
#undef cimg_use_openmp
#undef cimg_use_opencv
#include <CImg.h>

struct AIMandroidReadPort_t {
	bool success;
	int val;
};

class PictureTransformModule {
	public:
		PictureTransformModule();
		void Tick();
		void androidWritePort(int in);
//		int* androidReadPort();
		AIMandroidReadPort_t androidReadPort();

	private:
		std::vector<int> mWriteBuf;
		std::vector<int> mReadBuf;
		int mReadVal;
//		int mAndroidReadVal;

		int mIndex;
		cimg_library::CImg<int> mIntImg;

		void writePort(int out);
		int* readPort(bool blocking=false);
};
