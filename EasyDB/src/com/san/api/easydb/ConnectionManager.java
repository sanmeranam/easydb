package com.san.api.easydb;

import java.util.ArrayList;

import android.content.Context;

/**
 * 
 * @author Santanu Kumar
 */
final public class ConnectionManager {
	protected static ArrayList<Class> ENTITY_LIST=new ArrayList<Class>();
	private SQLHelper		mDBHelper;
	private Session			session;
	private static ConnectionManager CONNECTION_MANAGER;
	
	private ConnectionManager(Context context,String database) {
		this.mDBHelper = new SQLHelper(context,database,ENTITY_LIST);
	}

	public static ConnectionManager createConnectionManager(Context context,String database) {
		if(CONNECTION_MANAGER==null)
			CONNECTION_MANAGER=new ConnectionManager(context,database);
		
		return CONNECTION_MANAGER;
	}
	
	/**
	 * Register entity class.
	 * @param className
	 */
	public static void registerEntity(Class className) {
		if(className.getSuperclass().getName()==Entity.class.getName()){
			if(!ENTITY_LIST.contains(className))
				ENTITY_LIST.add(className);
		}else{
			System.err.print("! Invalid entity class. Entity class should subclass of com.san.api.easydb.Entity");
		}		
	}
	
	/**
	 * 
	 * @return
	 */
	public Session openSession(){
		session = new Session(mDBHelper.getWritableDatabase());
		return session;
	}

	/**
	 * 
	 */
	public void dispose() {
		if (mDBHelper != null) {
			mDBHelper.close();
		}
		CONNECTION_MANAGER=null;
	}
}
