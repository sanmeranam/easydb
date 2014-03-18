package com.san.api.easydb;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * @author Santanu Kumar
 */
public class Session {

	private EntityProcessor	entityProcessor;
	private SQLiteDatabase	connection;

	public Session(SQLiteDatabase connection) {
		this.entityProcessor = new EntityProcessor();
		this.connection = connection;
	}

	public void close() {
		if (this.connection != null){
			this.connection.close();
		}
	}

	/**
	 * Insert given entity class object.
	 * 
	 * @param b
	 * @return
	 */
	public int insert(Object b) {
		int index = 0;
		try {
			String sql = entityProcessor.getInsertSQL(b);

			SQLiteStatement st = connection.compileStatement(sql);
			connection.beginTransaction();
			index = (int) st.executeInsert();
			connection.setTransactionSuccessful();
			connection.endTransaction();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return index;
	}

	/**
	 * Delete entry of given entity object.
	 * 
	 * @param b
	 * @return
	 */
	public boolean update(Object b) {

		try {
			String sql = entityProcessor.getUpdateSQL(b);
			Log.d("SQL", sql);
			connection.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Delete entry of given entity object.
	 * 
	 * @param b
	 * @return
	 */
	public boolean delete(Object b) {

		try {
			String sql = entityProcessor.getDeleteSQL(b);
			connection.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Return fully qualified object with given partly qualified object
	 * matching.
	 * 
	 * @param d
	 * @return
	 */
	public Object get(Object d) {
		try {
			EntityProcessor.Entity_Meta meta = entityProcessor.getSelectSQL(d);
			Cursor r = connection.rawQuery(meta.SQL, new String[] {});

			if (r.moveToFirst()) {
				Object ins = d.getClass().newInstance();
				for (Field f : meta.fieldList) {
					Object resData = entityProcessor.callGetterMedhod("get" + entityProcessor.toCamelCase(f.getType().getSimpleName()), r, r.getColumnIndex(f.getName()));
					entityProcessor.callSetterMedhod("set" + entityProcessor.toCamelCase(f.getName()), ins, f.getType(), resData);
				}
				entityProcessor.callSetterMedhod("setId", ins, int.class, r.getInt(r.getColumnIndex("id")));
				d = ins;
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * Return fully qualified object list with given partly qualified object
	 * matching.
	 * 
	 * @param d
	 * @return
	 */
	public List<Object> list(Object d) {
		List resultList = new ArrayList();
		try {
			EntityProcessor.Entity_Meta meta = entityProcessor.getSelectSQL(d);
			Cursor r = connection.rawQuery(meta.SQL, new String[] {});
			if (r.moveToFirst())
				do {
					Object ins = d.getClass().newInstance();
					for (Field f : meta.fieldList) {
						Object resData = entityProcessor.callGetterMedhod("get" + entityProcessor.toCamelCase(f.getType().getSimpleName()), r, r.getColumnIndex(f.getName()));
						entityProcessor.callSetterMedhod("set" + entityProcessor.toCamelCase(f.getName()), ins, f.getType(), resData);

					}
					entityProcessor.callSetterMedhod("setId", ins, int.class, r.getInt(r.getColumnIndex("id")));
					resultList.add(ins);
				} while (r.moveToNext());
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * Return fully qualified object with given entity class name.
	 * 
	 * @param d
	 * @return
	 */
	public Object get(Class className) {

		Object ins = null;
		try {
			ins = className.newInstance();
			EntityProcessor.Entity_Meta meta = entityProcessor.getSelectSQL(className);
			Cursor r = connection.rawQuery(meta.SQL, new String[] {});

			if (r.moveToFirst()) {
				ins = className.newInstance();
				for (Field f : meta.fieldList) {
					Object resData = entityProcessor.callGetterMedhod("get" + entityProcessor.toCamelCase(f.getType().getSimpleName()), r, r.getColumnIndex(f.getName()));
					entityProcessor.callSetterMedhod("set" + entityProcessor.toCamelCase(f.getName()), ins, f.getType(), resData);
				}
				entityProcessor.callSetterMedhod("setId", ins, int.class, r.getInt(r.getColumnIndex("id")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ins;
	}

	/**
	 * Return fully qualified object list with given type entity class.
	 * 
	 * @param d
	 * @return
	 */
	public List list(Class className) {
		List list = new ArrayList();
		try {
			EntityProcessor.Entity_Meta meta = entityProcessor.getSelectSQL(className);
			Cursor r = connection.rawQuery(meta.SQL, new String[] {});
			if (r.moveToFirst())
				do {
					Object ins = className.newInstance();
					for (Field f : meta.fieldList) {
						Object resData = entityProcessor.callGetterMedhod("get" + entityProcessor.toCamelCase(f.getType().getSimpleName()), r, r.getColumnIndex(f.getName()));
						entityProcessor.callSetterMedhod("set" + entityProcessor.toCamelCase(f.getName()), ins, f.getType(), resData);

					}
					entityProcessor.callSetterMedhod("setId", ins, int.class, r.getInt(r.getColumnIndex("id")));
					list.add(ins);
				} while (r.moveToNext());
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
