package org.circedroid.log;

import android.content.Context;
import android.content.Intent;

public class LogManager {

	private static LogManager _instance;

	private LogManager() {
	}

	public static LogManager getInstance() {
		if (_instance == null) {
			_instance = new LogManager();
		}
		return _instance;
	}

	private Context _context;
	public static final String LOG_ACTION = "CirceLog";
	public static final String LOG_EXTRA = "myLog";

	public void setContext(Context context) {
		this._context = context;
	}

	private String logStack;
	
	private long lastLogTime = 0;
	private long intervalLog = 250;

	public void log(String message) {
		if (this._context == null) {
			return;
		}
		logStack = message+"\n"+logStack;
		if ((System.currentTimeMillis()-lastLogTime)>intervalLog){
			lastLogTime = System.currentTimeMillis();
			Intent logIntent = new Intent(LOG_ACTION);
			logIntent.putExtra(LOG_EXTRA, logStack);
			this._context.sendBroadcast(logIntent);
			logStack="";
		}
		
	}
}
