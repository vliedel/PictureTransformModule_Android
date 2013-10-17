package org.dobots.picturetransformmodule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final String MODULE_NAME = "PictureTransformModule";
	
//	Messenger mToMsgService = null;
//	final Messenger mFromMsgService = new Messenger(new IncomingMsgHandler());
//	boolean mMsgServiceIsBound;
	
//	Messenger mPortOutMessenger = null;
	
	TextView mCallbackText;
	Button mButtonStartStop;
	boolean mServiceIsRunning;
	
	
	// Copied from MsgService, should be an include?
	public static final int MSG_REGISTER = 1;
	public static final int MSG_UNREGISTER = 2;
	public static final int MSG_SET_MESSENGER = 3;
	public static final int MSG_START = 4;
	public static final int MSG_STOP = 5;
	public static final int MSG_SEND = 6;
	public static final int MSG_XMPP_LOGIN = 7;
	public static final int MSG_ADD_PORT = 8;
	public static final int MSG_REM_PORT = 9;
	public static final int MSG_XMPP_LOGGED_IN = 10;
	public static final int MSG_XMPP_DISCONNECTED = 11;
	public static final int MSG_PORT_DATA = 12;
	public static final int MSG_USER_LOGIN = 13;
	public static final int MSG_GET_MESSENGER = 14;
		
	public static final int DATATYPE_FLOAT = 1;
	public static final int DATATYPE_FLOAT_ARRAY = 2;
	public static final int DATATYPE_STRING = 3;
	public static final int DATATYPE_IMAGE = 4;
	public static final int DATATYPE_BINARY = 5;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mCallbackText = (TextView) findViewById(R.id.messageOutput);
        mButtonStartStop = (Button) findViewById(R.id.buttonStartStop);
        
        mButtonStartStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		        if (mServiceIsRunning) {
		        	stopService();
		        }
		        else {
		        	startService();
		        }
		        checkServiceRunning();
		    }
		});
        
                
        checkServiceRunning();
       
//        doBindService();
        
//        Integer id = 0;
//        new AIMRun().execute(id);
        
    }
    
	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG,"onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG,"onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG,"onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i(TAG,"onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		Log.i(TAG, "onDestroy " + mMsgServiceIsBound);
		Log.i(TAG, "onDestroy");
//		doUnbindService();
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void startService() {
        Intent intent = new Intent();
        intent.setClassName("org.dobots.picturetransformmodule", "org.dobots.picturetransformmodule.PictureTransformModuleService");
        ComponentName name = startService(intent);
        Log.i(TAG, "Starting: " + name.toString());
    }
    
    public void stopService() {
    	Intent intent = new Intent();
        intent.setClassName("org.dobots.picturetransformmodule", "org.dobots.picturetransformmodule.PictureTransformModuleService");
        stopService(intent);
        Log.i(TAG, "Stopping service: " + intent.toString());
//        finish();
    }
    
    private boolean checkServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (PictureTransformModuleService.class.getName().equals(service.service.getClassName())) {
            	mServiceIsRunning = true;
            	mButtonStartStop.setText("Stop module");
            	mCallbackText.setText("Module is running");
                return true;
            }
        }
        mServiceIsRunning = false;
        mButtonStartStop.setText("Start module");
        mCallbackText.setText("Module stopped");
        return false;
    }
    
/*    
    
    // ----- Copy & pasted -----
	private ServiceConnection mMsgServiceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the service has been established, giving us the service object
			// we can use to interact with the service.  We are communicating with our service through an IDL 
			// interface, so get a client-side representation of that from the raw service object.
			mToMsgService = new Messenger(service);
			mCallbackText.setText("Connected to Dodedodo.");

			Message msg = Message.obtain(null, MSG_REGISTER);
			Bundle bundle = new Bundle();
			bundle.putString("module", MODULE_NAME);
			bundle.putInt("id", 0);
			msg.setData(bundle);
			msgSend(msg);
			
//	        Toast.makeText(Binding.this, R.string.remote_service_connected, Toast.LENGTH_SHORT).show();
			Log.i(TAG, "Connected to MsgService: " + mToMsgService.toString());
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been unexpectedly disconnected: its process crashed.
			mToMsgService = null;
			mCallbackText.setText("Disconnected from Dodedodo.");

//	        Toast.makeText(Binding.this, R.string.remote_service_disconnected, Toast.LENGTH_SHORT).show();
			Log.i(TAG, "Disconnected from MsgService");
		}
	};

	// Handle messages from MsgService
	class IncomingMsgHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SET_MESSENGER:
				Log.i(TAG, "set port: " + msg.getData().getString("port") + " to: " + msg.replyTo.toString());
				if (msg.getData().getString("port").equals("out"))
					mPortOutMessenger = msg.replyTo;
				break;
			case MSG_STOP:
				Log.i(TAG, "stopping");
				finish();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	void doBindService() {
		// Establish a connection with the service.  We use an explicit class name because there is no reason to be 
		// able to let other applications replace our component.
		//bindService(new Intent(this, XMPPService.class), mConnection, Context.BIND_AUTO_CREATE);
		
		Intent intent = new Intent();
		intent.setClassName("org.dobots.dodedodo", "org.dobots.dodedodo.MsgService");
//		intent.setClassName(".dodedodo", "MsgService");
		bindService(intent, mMsgServiceConnection, Context.BIND_AUTO_CREATE);
		mMsgServiceIsBound = true;
		mCallbackText.setText("Binding to service.");
	}

	void doUnbindService() {
		if (mMsgServiceIsBound) {
			// If we have received the service, and registered with it, then now is the time to unregister.
			if (mToMsgService != null) {
				Message msg = Message.obtain(null, MSG_UNREGISTER);
				Bundle bundle = new Bundle();
				bundle.putString("module", MODULE_NAME);
				bundle.putInt("id", 0);
				msg.setData(bundle);
				msgSend(msg);
			}
			// Detach our existing connection.
			unbindService(mMsgServiceConnection);
			mMsgServiceIsBound = false;
			mCallbackText.setText("Unbinding from service.");
		}
	}

	protected void msgSend(Message msg) {
		if (!mMsgServiceIsBound) {
			Log.i(TAG, "Can't send message to service: not bound");
			return;
		}
		try {
			msg.replyTo = mFromMsgService;
			mToMsgService.send(msg);
		} catch (RemoteException e) {
			Log.i(TAG, "Failed to send msg to service. " + e);
			// There is nothing special we need to do if the service has crashed.
		}
	}
	
	protected void msgSend(Messenger messenger, Message msg) {
		if (messenger == null)
			return;
		try {
			msg.replyTo = mFromMsgService;
			messenger.send(msg);
		} catch (RemoteException e) {
			Log.i(TAG, "failed to send msg to service. " + e);
			// There is nothing special we need to do if the service has crashed.
		}
	}

//	public void sendMessage() {
//		// Do something in response to button click
//		String text = mEditText.getText().toString();
//		if (TextUtils.isEmpty(text))
//			return;
//		Message msg = Message.obtain(null, MSG_PORT_DATA);
//		Bundle bundle = new Bundle();
//		bundle.putInt("datatype", DATATYPE_STRING);
//		bundle.putString("data", text);
//		msg.setData(bundle);
//		msgSend(mPortOutMessenger, msg);
//		mEditText.getText().clear();
//	}
	// ----- End copy & paste
    
    
	// AsyncTask<Params, Progress, Result>
	private class AIMRun extends AsyncTask<Integer, Void, Boolean> {
		protected Boolean doInBackground(Integer... id) {
			int input = 3;
			PictureTransformModule aim = new PictureTransformModule();
			AIMandroidReadPort_t output;
			while (true) {
				aim.androidWritePort(input);
				aim.Tick();
				output = aim.androidReadPort();
				if (output.getSuccess()) {
					Log.i(TAG, "Output=" + output.getVal());
				}
				if (isCancelled()) break;
			}
			return true;
		}
		
		protected void onPostExecute() {
			Log.i(TAG, "Stopped AIMRun");
		}
		
	}
    
    
    // static constructor
    static {
        System.loadLibrary("PictureTransformModule");
    }
    */
}
