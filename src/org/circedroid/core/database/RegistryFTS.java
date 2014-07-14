package org.circedroid.core.database;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.circedroid.core.geodesy.CRS;
import org.circedroid.core.register.Register;
import org.circedroid.core.tools.StringUtils;
import org.circedroid.log.LogManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class RegistryFTS implements Serializable{
	

	private static final long serialVersionUID = -6601027442073902300L;

	private static final String LOG_TAG = RegistryFTS.class.getSimpleName();
	
	private RegistryFTSOpenHelper helper;
	
	private SQLiteDatabase database;

	private RegistryFTS(Context context) {
		this.helper = new RegistryFTSOpenHelper(context);
	}
	private static RegistryFTS instance = null;
	
	public static RegistryFTS getInstance(Context context){
		if (instance == null){
			instance = new RegistryFTS(context);
		}
		return instance;
	}

	private class RegistryFTSOpenHelper extends SQLiteOpenHelper implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5814570632099022861L;
		private static final String DATABASE_NAME = "Registry_FTS";
		private static final int DATABASE_VERSION = 1;
		private static final String TABLE_NAME = "searchCRS";
		private static final String ID_KEY = "id";
		private static final String ALIASES_KEY = "aliases";
		private static final String NAME_KEY = "name";
		private static final String CREATE_TABLE = "create virtual table "
				+ TABLE_NAME + " using fts3(" + ID_KEY + " text primary key, "
				+ ALIASES_KEY + " text, " + NAME_KEY + " text);";
		private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME+";";

		public RegistryFTSOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_TABLE);
			onCreate(db);

		}

	}

	
	public void indexCRS(Map<String,CRS> listcrs){
		SQLiteDatabase db = this.helper.getWritableDatabase();
		//delete * from table;
		db.delete(RegistryFTSOpenHelper.TABLE_NAME, "1", null);
		Log.d(LOG_TAG, "Delete all row");

		for (CRS it : listcrs.values()){
			ContentValues cv = new ContentValues();
			cv.put(RegistryFTSOpenHelper.ID_KEY, it.getId());
			cv.put(RegistryFTSOpenHelper.ALIASES_KEY, listToString(it.getAliases()));
			cv.put(RegistryFTSOpenHelper.NAME_KEY, StringUtils.normalize(it.getName()));
			
			if (!db.isOpen()){
				db.close();
				db = this.helper.getWritableDatabase();
			}
			if (db.replace(RegistryFTSOpenHelper.TABLE_NAME, null, cv)!=-1){
				Log.d(LOG_TAG, "- crs "+it.getId()+" indexed");
				LogManager.getInstance().log( "- crs "+it.getId()+" indexed");
			} else {
				Log.e(LOG_TAG, "- crs "+it.getId()+" NOT-indexed");
			}
		}
		db.close();
		
	}
	
	private String listToString(List<String> list){
		StringBuilder sb = new StringBuilder();
		for (String it : list){
			sb.append(" ");
			sb.append(it);
		}
		return sb.toString();
	}
	
	public Cursor search(String search){
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(RegistryFTSOpenHelper.TABLE_NAME);
		
		database = this.helper.getReadableDatabase();
		String selection = RegistryFTSOpenHelper.TABLE_NAME + " MATCH ?";
		search = ("*"+search.replaceAll(" ", "* *")+"*").replaceAll("\\*\\*", "*");
		Log.d(LOG_TAG, "look for '"+search+"'");
	    String[] selectionArgs = new String[] {StringUtils.normalize(search)};
		String[] column = new String[] { RegistryFTSOpenHelper.ID_KEY };
		return qb.query(database, column, selection, selectionArgs, null, null,null);
		
	}
	
	public void closeDB(){
		if (this.database != null) {
			this.database.close();
		}
	}

}
