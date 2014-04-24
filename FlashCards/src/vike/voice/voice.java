package vike.voice;

import java.io.IOException;
import java.util.Locale;

import vike.flash.cards.Preferences;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;




public class voice {

	private TextToSpeech mSpeech;
	//private MediaPlayer mPlayer;
	private Context mCtx;
	
	public voice(Context context){
		mCtx=context;
	}

	public void InitVoice() {
		mSpeech = new TextToSpeech(mCtx, new OnInitListener() {

			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int result = mSpeech.setLanguage(Locale.ENGLISH);

					if (result == TextToSpeech.LANG_MISSING_DATA|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
						Log.e("lanageTag", "not use");
					}
				}
			}
		});
	}

	public void speak(String str){
		MediaPlayer mPlayer;
		Preferences Pfs=new Preferences();
		@SuppressWarnings("static-access")
		SharedPreferences sp = Pfs.getPreferenceManager().getDefaultSharedPreferences(mCtx);
		Boolean netvoice=sp.getBoolean("voice", false);
		
		boolean wificonn = isWifiConnected(mCtx);
		boolean moblieconn = isMobileConnected(mCtx);
		String googleTran="http://translate.google.com/translate_tts?q=";
		String[] st = str.split(" ");
		str=new String();
		for(String s:st){
			str=str+s;
			//str=str+"-";
		}
		if ((moblieconn==false&&wificonn==false)||!netvoice){
			mSpeech.speak(str,TextToSpeech.QUEUE_FLUSH, null);
		}	
		else {
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						mp.release();
					}
				});
				mPlayer.setDataSource(googleTran+str);
				mPlayer.prepare();
				mPlayer.start();
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
	
	public static boolean isWifiConnected(Context context) { 
		return getNetworkState(context, ConnectivityManager.TYPE_WIFI) == State.CONNECTED;
	}
	public static boolean isMobileConnected(Context context) {
		return getNetworkState(context, ConnectivityManager.TYPE_MOBILE) == State.CONNECTED;
	}

	private static State getNetworkState(Context context, int networkType) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cm.getNetworkInfo(networkType);

		return info == null ? null : info.getState();        
	}


}