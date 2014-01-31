package com.teenscribblers.scrabbleton;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseAssist extends SQLiteAssetHelper {
	private static final String DATABASE_NAME = "words.db";
	private static final int DATABASE_VERSION = 1;
	String[] sqlSelect = { "_id", "word" };
	String sqlTables = "words";

	public DatabaseAssist(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	public Cursor getWords() {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

		c.moveToFirst();
		return c;

	}

	public Cursor getRandomWords() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + sqlTables
				+ " ORDER BY RANDOM() LIMIT 100", null);
		if (cursor.moveToFirst()) {
			return cursor;
		}
		return cursor;
	}

	public Cursor getWordsByLetter(String chara) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + sqlTables
				+ " WHERE Upper(substr(word,1,1)) LIKE '" + chara + "'", null);
		if (cursor.moveToFirst()) {
			return cursor;
		}
		return cursor;
	}
	public Cursor getWordsBycount(int n) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + sqlTables
				+ " WHERE length(word) =" + n+" ORDER BY RANDOM()", null);
		if (cursor.moveToFirst()) {
			return cursor;
		}
		return cursor;
	}
	
	public Cursor getmatchingwords(String inputText) throws SQLException{
		SQLiteDatabase db = getReadableDatabase();
		  Cursor mCursor = null;
		  if (inputText == null  ||  inputText.length () == 0)  {
		   mCursor = db.query(sqlTables,sqlSelect ,
		     null, null, null, null, null);
		 
		  }
		  else {
		   mCursor = db.query(true, sqlTables, sqlSelect,
		     "word like '%" + inputText + "%'", null,
		     null, null, null, null);
		  }
		  if (mCursor != null) {
		   mCursor.moveToFirst();
		  }
		  return mCursor;
	}

}
