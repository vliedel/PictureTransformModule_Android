#include <vector>

// CImg configuration
#define cimg_verbosity 2
#define cimg_display 0
#undef cimg_use_openmp
#undef cimg_use_opencv
#include <CImg.h>

#define DEBUG_STREAM
#ifdef DEBUG_STREAM
#include <cstdio>
#include <iostream>
#include <sstream>
struct Streamer {
  virtual void display(std::string text) const = 0;
  virtual ~Streamer() {}
};
void setStreamer(Streamer* streamer);
Streamer& getStreamer();
template<typename T> Streamer& operator<<(Streamer& stream, T const& val) {
  std::ostringstream s;
  s << val;
  stream.display(s.str());
  return stream;
}
#endif

struct AIMandroidReadPort_t {
	bool success;
	int val;
};

struct AIMandroidReadSeqPort_t {
	bool success;
	std::vector<float> val;
};

class PictureTransformModule {
	public:
		PictureTransformModule();
		void Tick();
		void androidWritePort(int in);
//		int* androidReadPort();
		AIMandroidReadPort_t androidReadPort();

		void androidWriteSeqPort(std::vector<float> in);
		AIMandroidReadSeqPort_t androidReadSeqPort();

	private:
		std::vector<int> mWriteBuf;
		std::vector<int> mReadBuf;

		std::vector<std::vector<float> > mWriteSeqBuf;
		std::vector<std::vector<float> > mReadSeqBuf;

		int mReadVal;
//		int mAndroidReadVal;
		std::vector<float> mReadSeqVal;

		int mIndex;
		cimg_library::CImg<int> mIntImg;

		void writePort(int out);
		int* readPort(bool blocking=false);
		void writeSeqPort(std::vector<float> &out);
		std::vector<float>* readSeqPort(bool blocking=false);
};
