package com.san.api.easydb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Santanu Kumar
 */
public class EntityProcessor {
	/**
	 * Return SELECT SQL based on entity class given
	 * 
	 * @param object
	 * @return
	 */
	public Entity_Meta getSelectSQL(Object object) throws Exception {

		Field field[] = object.getClass().getDeclaredFields();
		Entity_Meta meta = new Entity_Meta();

		if (!validate(field)) {
			throw new IllegalArgumentException("Invalid entity class.Check table and id field.");
		}
		StringBuilder sbValueSeq = new StringBuilder();
		sbValueSeq.append(" WHERE ");
		String tableName = object.getClass().getSimpleName().toLowerCase(), id = null;
		meta.table_name = tableName;

		for (Field f : field) {
			if (f.getName().equalsIgnoreCase("id") && f.getType() == int.class) {
				String retValu = callGetterMedhod("getId", object);
				if (retValu.equals("0")) {
					id = null;
				} else {
					id = " WHERE id='" + retValu + "'";
				}
				meta.id = f.getName();
			} else {
				sbValueSeq.append(f.getName()).append("='").append(callGetterMedhod("get" + toCamelCase(f.getName()), object)).append("' and ");
				meta.addField(f);
			}
		}
		String fieldList = sbValueSeq.toString();
		fieldList = fieldList.substring(0, fieldList.lastIndexOf("and"));
		meta.SQL = "SELECT * FROM " + tableName + ((id == null) ? fieldList : id);
		return meta;
	}

	/**
	 * Return SELECT SQL based on entity class given
	 * 
	 * @param object
	 * @return
	 */
	public Entity_Meta getSelectSQL(Class className) throws Exception {
		Field field[] = className.getDeclaredFields();

		Entity_Meta meta = new Entity_Meta();

		if (!validate(field)) {
			throw new IllegalArgumentException("Invalid entity class.Check table and id field.");
		}

		String tableName = className.getSimpleName().toLowerCase();
		meta.table_name = tableName;

		for (Field f : field) {
			if (f.getName().equalsIgnoreCase("id") && f.getType() == int.class) {
				meta.id = f.getName();
			} else {
				meta.addField(f);
			}
		}
		meta.SQL = "SELECT * FROM " + tableName;
		return meta;
	}

	/**
	 * Return DELETE SQL based on entity class.
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public String getDeleteSQL(Object object) throws Exception {
		Field field[] = object.getClass().getDeclaredFields();

		if (!validate(field)) {
			throw new IllegalArgumentException("Invalid entity class.Check table and id field.");
		}
		StringBuilder sbValueSeq = new StringBuilder();
		String tableName = object.getClass().getSimpleName().toLowerCase(), id = null;

		for (Field f : field) {
			if (f.getName().equalsIgnoreCase("id") && f.getType() == int.class) {
				String retValu = callGetterMedhod("getId", object);
				if (retValu.equals("0")) {
					id = null;
				} else {
					id = " WHERE id=" + retValu + "";
				}
			} else {
				sbValueSeq.append(f.getName()).append("='").append(callGetterMedhod("get" + toCamelCase(f.getName()), object)).append("' and ");
			}
		}
		String valueList = sbValueSeq.toString();

		if (id != null) {
			return "DELETE FROM " + tableName + id;
		} else {
			return "DELETE FROM " + tableName + " WHERE " + valueList.substring(0, valueList.lastIndexOf("and"));
		}
	}

	/**
	 * Return INSERT SQL statement of given entity object.
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public String getInsertSQL(Object object) throws Exception {
		Field field[] = object.getClass().getDeclaredFields();

		if (!validate(field)) {
			throw new IllegalArgumentException("Invalid entity class.Check table and id field.");
		}

		StringBuilder sbFieldSeq = new StringBuilder();
		StringBuilder sbValueSeq = new StringBuilder();

		String tableNam = object.getClass().getSimpleName().toLowerCase();

		for (Field f : field) {
			if (f.getName().equalsIgnoreCase("id") && f.getType() == int.class) {
				// ignore
			} else {
				sbFieldSeq.append(f.getName()).append(",");
				sbValueSeq.append("'").append(callGetterMedhod("get" + toCamelCase(f.getName()), object)).append("',");
			}
		}
		String fieldList = sbFieldSeq.toString();
		String valueList = sbValueSeq.toString();

		return "INSERT INTO " + tableNam + " (" + fieldList.substring(0, fieldList.lastIndexOf(",")) + ") VALUES(" + valueList.substring(0, valueList.lastIndexOf(",")) + ")";
	}

	/**
	 * Return UPDATE SQL based on entity class.
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public String getUpdateSQL(Object object) throws Exception {
		Field field[] = object.getClass().getDeclaredFields();

		if (!validate(field)) {
			throw new IllegalArgumentException("Invalid entity class.Check table and id field.");
		}
		StringBuilder sbValueSeq = new StringBuilder();
		String tableName = object.getClass().getSimpleName().toLowerCase(), id = null;

		sbValueSeq.append(" SET ");

		for (Field f : field) {
			if (f.getName().equalsIgnoreCase("id") && f.getType() == int.class) {
				String retValu = callGetterMedhod("getId", object);
				if (retValu.equals("0")) {
					id = null;
				} else {
					id = " WHERE id='" + retValu + "'";
				}
			} else {
				sbValueSeq.append(f.getName()).append("='").append(callGetterMedhod("get" + toCamelCase(f.getName()), object)).append("',");
			}
		}
		String valueList = sbValueSeq.toString();
		if (id == null) {
			throw new IllegalArgumentException("Primary id not specified.");
		} else {
			valueList = valueList.substring(0, valueList.lastIndexOf(","));
			return "UPDATE " + tableName + valueList + id;
		}

	}

	/**
	 * Call method and returns return value of called method of given object.
	 * 
	 * @param name
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public String callGetterMedhod(String name, Object object) throws Exception {
		Method method = object.getClass().getMethod(name, null);
		Object res = method.invoke(object);
		return res == null ? "NULL" : res.toString();
	}

	/**
	 * 
	 * @param name
	 * @param object
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	public Object callGetterMedhod(String name, Object object, int arg) throws Exception {
		Method method = object.getClass().getMethod(name, int.class);
		Object res = method.invoke(object, arg);
		return res;
	}

	/**
	 * 
	 * @param name
	 * @param object
	 * @param type
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String callSetterMedhod(String name, Object object, Class type, Object value) throws Exception {
		Method method = object.getClass().getMethod(name, type);
		Object res = method.invoke(object, value);
		return res == null ? "NULL" : res.toString();
	}

	public String getDropSQL(Class cn) {
		return "DROP TABLE IF EXISTS " + cn.getSimpleName().toLowerCase();
	}

	/**
	 * Return CREATE SQL based on entity class.
	 * 
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public String getCreateSQL(Class className) throws Exception {
		Field field[] = className.getDeclaredFields();
		StringBuilder sb = new StringBuilder();

		String tableName = className.getSimpleName().toLowerCase(), id = null;

		for (Field f : field) {
			if (f.getName().equalsIgnoreCase("id") && f.getType() == int.class) {
				id = "id INTEGER PRIMARY KEY";
			} else {
				sb.append(f.getName()).append(" ").append(getTypeString(f)).append(",");
			}
		}
		String fieldList = sb.toString();
		if (tableName == null) {
			throw new IllegalArgumentException("Entity class not contain table name. req TABLE_NAME with String static field.");
		}
		if (id == null) {
			throw new IllegalArgumentException("Entity class not contain primary key \"id\".");
		}
		String result = "CREATE TABLE " + tableName + "(" + id + "," + fieldList.substring(0, fieldList.lastIndexOf(",")) + ")";
		return result;
	}

	/**
	 * Return SQL type of given class field.
	 * 
	 * @param f
	 * @return
	 */
	public String getTypeString(Field f) {
		String type = null;
		if (f.getType() == int.class) {
			type = "INTIGER";
		} else if (f.getType() == float.class) {
			type = "REAL";
		} else if (f.getType() == String.class) {
			type = "TEXT";
		} else if (f.getType() == boolean.class) {
			type = "NUMERIC";
		} else if (f.getType() == Date.class) {
			type = "NUMERIC";
		} else if (f.getType() == Object.class) {
			type = "BLOB";
		} else {
			type = "NULL";
		}
		return type;
	}

	/**
	 * Entity class validation
	 * 
	 * @param list
	 * @return
	 */
	public boolean validate(Field[] list) {
		for (Field f : list) {
			if (f.getName().equalsIgnoreCase("id") && f.getType() == int.class) {
				return true;
			}
		}
		return false;
	}

	/**
	 * To camel case of given string
	 * 
	 * @param s
	 * @return
	 */
	public String toCamelCase(String s) {
		String[] parts = s.split("_");
		String camelCaseString = "";
		for (String part : parts) {
			camelCaseString = camelCaseString + toProperCase(part);
		}
		return camelCaseString;
	}

	/**
	 * first char to upper
	 * 
	 * @param s
	 * @return
	 */
	public String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * Entity meta data class for select
	 */
	public static class Entity_Meta {

		String				SQL;
		String				table_name;
		String				id;
		ArrayList<Field>	fieldList;

		public Entity_Meta() {
			fieldList = new ArrayList<Field>();
		}

		public void addField(Field f) {
			fieldList.add(f);
		}

		@Override
		public String toString() {
			return "Entity_Meta{" + "SQL=" + SQL + ", table_name=" + table_name + ", id=" + id + ", fieldList=" + fieldList.size() + '}';
		}
	}
}
