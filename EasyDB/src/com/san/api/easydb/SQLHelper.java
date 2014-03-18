package com.san.api.easydb;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {

	private ArrayList<Class>			entityList;
	private EntityProcessor	mEntityProcessor;

	public SQLHelper(Context context,String dataBaseName, ArrayList<Class> entityList) {
		super(context, dataBaseName, null, 1);
		
		this.entityList=entityList;
		this.mEntityProcessor = new EntityProcessor();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			for (Class c : entityList) {
				db.execSQL(mEntityProcessor.getCreateSQL(c));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			for (Class c : entityList) {
				db.execSQL(mEntityProcessor.getDropSQL(c));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		onCreate(db);
	}

}
