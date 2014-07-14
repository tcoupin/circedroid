package org.circedroid.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.circedroid.core.register.Register;


import android.content.Context;
import android.util.Log;

public class RegisterManager {

	private static final String LOG_TAG = RegisterManager.class.getSimpleName();
	
	public static final String Register_FILE_NAME = "RegSerial.obj";

	private static Register _instance = null;
	private static Context _context = null;

	/**
	 * Load the serialized Register or return if already loaded
	 *
	 * @return the loaded Register or null if problem happen 
	 */
	public static Register getInstance() {
		if (_instance == null) {
			_instance = deserializeReg();
		}
		return _instance;
	}

	public static void setContext(Context context) {
		_context = context;
	}
	public static Context getContext() {
		return _context;
	}

	private static File getFile() {
		String path = _context.getFilesDir().getAbsolutePath();
		File regSerial = new File(path + "/"
				+ RegisterManager.Register_FILE_NAME);
		return regSerial;
	}

	private static Register deserializeReg() {
		Log.i(LOG_TAG, "Start deserialize Register");
		File file = getFile();

		Register reg = null;
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			reg =  (Register) ois.readObject();
			ois.close();
		} catch (IOException e) {
			Log.e(LOG_TAG,e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e(LOG_TAG,e.getMessage());
		}
		Log.i(LOG_TAG, "End deserialize Register");
		return reg;
	}

	private static void serializeReg(Register Register) {
		Log.i(LOG_TAG, "Start serialize Register");
		File file = getFile();
		try {
			if (!file.exists()) {
				Log.i(LOG_TAG, " - file does not exist");
				file.createNewFile();
			}
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(file));
			oos.writeObject(Register);
			oos.close();
		} catch (IOException e) {
			Log.e(LOG_TAG,e.getMessage());
		}
		Log.i(LOG_TAG, "End serialize Register");
	}
	
	/**
	 * Save the current Register
	 */
	public static void save(){
		serializeReg(_instance);
	}

	/**
	 * Initialize the Register from XML file.
	 * @param context the context of the activity
	 * @param is the input stream of file
	 * @return
	 */
	public static boolean initXMLRegister(Context context, InputStream is) {
		Register reg = new Register(context, is);
		if (reg != null) {
			_instance = reg;
			return true;
		}
		return false;
	}

}
