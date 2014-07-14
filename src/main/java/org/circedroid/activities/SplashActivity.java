package org.circedroid.activities;

import org.circedroid.R;
import org.circedroid.core.RegisterManager;
import org.circedroid.core.register.Register;
import org.circedroid.log.LogManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private static String LOG_TAG = SplashActivity.class.getSimpleName();

	boolean firstload = true;

	private static TextView consoleTV;

	private BroadcastReceiver bReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		consoleTV = (TextView) findViewById(R.id.splash_console);
		this.bReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				try {
					log(intent.getStringExtra(LogManager.LOG_EXTRA));
				} catch (Exception e){
					
				}
				
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!this.firstload) {
			Log.d(LOG_TAG, " second load => kill");
			finish();
			return;
		} else {
			Log.d(LOG_TAG, " first load");
		}
		RegisterManager.setContext(this);
		
		//Init custom log.
		LogManager.getInstance().setContext(getApplicationContext());
		registerReceiver(bReceiver, new IntentFilter(LogManager.LOG_ACTION));
		
		Log.d(LOG_TAG, "Get Reg instance");
		Register reg = RegisterManager.getInstance();
		if (reg != null) {
			Log.d(LOG_TAG, "startActivity");
			startTransformActivity();
		} else {
			Log.d(LOG_TAG, "init");
			AsyncInit task = new AsyncInit();
			task.execute();
		}
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		unregisterReceiver(bReceiver);
	}

	private void startTransformActivity() {
		firstload = false;
		Intent intent = new Intent(this, TransformActivity.class);
		this.startActivity(intent);
	}

	public void log(String message) {
		int maxSize = 100;
		if (message.length() > maxSize) {
			message.substring(0, maxSize);
		}
		consoleTV.setText(message);
	}

	private class AsyncInit extends AsyncTask<Void, String, Boolean> {


		@Override
		protected Boolean doInBackground(Void... params) {
			RegisterManager.initXMLRegister(getApplicationContext(),
					getResources().openRawResource(R.raw.ignf));

			RegisterManager.save();
			Register reg = RegisterManager.getInstance();
			return (reg != null);
		}


		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Log.d(LOG_TAG, "xml loaded");
			if (result) {
				Log.d(LOG_TAG, "start TransformActivity");
				startTransformActivity();
			} else {
				Log.d(LOG_TAG, "Display error message");
				String errormessage = getString(R.string.loading_problem);
				TextView tv = (TextView) findViewById(R.id.splash_text);
				tv.setText(errormessage);

				Drawable draw = getResources().getDrawable(
						android.R.drawable.ic_dialog_alert);

				ProgressBar pb = (ProgressBar) findViewById(R.id.splash_icon);
				LinearLayout ll = (LinearLayout) findViewById(R.id.splash_layout);
				ll.removeView(pb);

				ImageView iv = new ImageView(SplashActivity.this);
				iv.setImageDrawable(draw);
				ll.addView(iv, 0);

			}
		}
	}

}

