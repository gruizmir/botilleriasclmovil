package me.emborrachar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class Splash extends Activity {
	private Handler mHandler;
	boolean AUTO_CLOSE = true;
	int TIME_CLOSE = 3000;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		mHandler = new Handler();
		mHandler.postDelayed(mUpdateTimeTask, TIME_CLOSE);
	}

	/**
	 * Runnable encargado de cerrar automaticamente la aplicacion
	 */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			if(AUTO_CLOSE){
				finish();
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return false;
	}
}