We know the steps and complexity of create and use database in android application. Now this API makes easy to use database and developer friendly object value to use. Very simple way to use this API, create your entity class (Setter & Getter class), Add an entry to Entity Processor, and use.

# Easydb
Develop your android application with database, and here is the library allow you to create you database configuration easy. Forget about query, forget about query syntax. Use your *Entity* class like other java database persistence framework. It's implemented with very light wight, easy, simple code. 

* No create database
* No upgrade query
* No select, update, delete, insert query.

### Initialization
Only three easy steps to get start with *Easydb*. 

1. Include **Easydb** to your android project. **Easydb** is a android library project, so easily can be included to your project. Also you can copy classes to your project, as its not using any external dependencies.
2. Create your **Entity** class by extending `com.san.api.easydb.Entity` without identifier.
```java
public class MyEntityClass extends Entity {

	//No need of ID field. Implicitly its available.
	//Use camel case for member variables.
	//Don't use underscore(_) in mber variable
	private String title;
	private String subTitle;
	private String tag;
...
```
3. Register your **Entity** class to `ConnectionManager`.
```java
//Register entity class with ConnectionManager
static{
	ConnectionManager.registerEntity(MyEntityClass.class);
}
```
Now its ready to use in your code for any kind of database transactions.

### How to use?
1. Create `ConnectionManager` with `Context` and *database* name.
```java
public class TestActivity extends Activity {

	public static final String DATABASE="sample.db";
	private ConnectionManager manager;
	...
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		//Creating connection manage , Arg1: Context , Arg2: Database name
                //com.san.api.easydb.ConnectionManager
		manager=ConnectionManager.createConnectionManager(this, TestActivity.DATABASE);

```
2. Open `Session` using `com.san.api.easydb.ConnectionManager` and use transaction methods.

```java
       private void initList() {
		//Opening a data base session 
		com.san.api.easydb.Session session=manager.openSession();
		/**
		 * To get all records, only need to pass the registered entity class.
		 */
		listData = session.list(MyEntityClass.class);

		MyListAdapter listAdapter = new MyListAdapter(this, listData);
		listView.setAdapter(listAdapter);

		//Need to close session post use
		session.close();
	}
```

###Other Session Transactions
```java
com.san.api.easydb.Session session=manager.openSession();

//Creating new Entity object and setting values.
MyEntityClass entityObject =new MyEntityClass();
entityObject.setTitle("Title1");
entityObject.setSubTitle("SubTitle1");
entityObject.setTag("Tag1");

//Inserting new row
int rowId=session.insert(entityObject);

entityObject.setId(rowId);
entityObject.setTag("Tag2");//update value for same record

//Updating exiting record with identifier.
session.update(entityObject);

//Removing record from database with identifier
session.delete(entityObject);

//Fetching all records in database
List recordList1=session.list(MyEntityClass.class);

MyEntityClass obj1=new  MyEntityClass();
obj1.setTag("Tag");

//Fetching all records in database which having tag="Tag"
List recordList2=session.list(obj1);

//Fetching first record having tag="Tag"
obj1=session.get(obj1);

//Delete all records having tag="Tag". Without identifier.
session.delete(obj1);
```
### Session transaction methods and usability.
|Methods|`Class` arg|`Object` arg without id|`Object` arg with id|Returns|
|-------|------------|-------------------------|----------------------|-------|
|`session.insert()`|-|✓|✓|`int` new row id|
|`session.delete()`|-|✓|✓|`boolean` true or false|
|`session.update()`|-|✓|✓|`boolean` true or false|
|`session.get()`|✓|✓|✓|`Object` or `null`|
|`session.list()`|✓|✓|✓|`List` of `Object`|
**Table Name** : It's use Entity class name as table name implicitly.

**Note** : Like other java based database persistence framework,it's don't support related *database*.It's only support single table based database. In case you project need table relationship, you have do handle same in your logic.

Save complexity, Save time and Enjoy.
