#include <vector>

struct AIMandroidReadPort_t {
	bool success;
	int val;
};

class PictureTransformModule {
	public:

		void Tick();
		void androidWritePort(int in);
//		int* androidReadPort();
		AIMandroidReadPort_t androidReadPort();

	private:
		std::vector<int> mWriteBuf;
		std::vector<int> mReadBuf;
		int mReadVal;
//		int mAndroidReadVal;

		void writePort(int out);
		int* readPort(bool blocking=false);
};
