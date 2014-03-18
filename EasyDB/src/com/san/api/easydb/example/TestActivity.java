package com.san.api.easydb.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.san.api.easydb.ConnectionManager;
import com.san.api.easydb.R;
import com.san.api.easydb.Session;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class TestActivity extends Activity {

	public static final String DATABASE="sample.db";
	private ConnectionManager manager;
	private ActionMode			actionMode;
	private List<MyEntityClass>	listData;
	private ListView			listView;
	private int					position	= -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		manager=ConnectionManager.createConnectionManager(this, TestActivity.DATABASE);
		
		listView = (ListView) findViewById(R.id.listView1);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v, int pos, long id) {
				if (actionMode != null) {
					return false;
				}
				position=pos;
				actionMode = startActionMode(mActionModeCallback);
				v.setSelected(true);
				return true;
			}
		});
		
		initList();
	}
	/**
	 * List box initialization.
	 */
	private void initList() {
		Session session=manager.openSession();
		/**
		 * To get all records, only need to pass the registered entity class.
		 */
		listData = session.list(MyEntityClass.class);
		MyListAdapter listAdapter = new MyListAdapter(this, listData);
		listView.setAdapter(listAdapter);
		
		session.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuItemAdd:
				ShowDialogBox();
			break;
		}
		return true;
	}

	/**
	 * List item context menu callback.
	 */
	private ActionMode.Callback	mActionModeCallback	= new ActionMode.Callback() {
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.context_menu, menu);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
						case R.id.menuItemDelete:
							if(position!=-1 && listData!=null){
								
								MyEntityClass dataObject=listData.get(position);
								
								
								Session session=manager.openSession();
								//Delete existing record by passing fully qualified entity object.
								//if ID match, then its not looking for other fields, of id is not there, then
								//its look for match records based on existing fields.
								session.delete(dataObject);
								
								session.close();
								initList();
							}
							mode.finish();
							return true;
						default:
							return false;
					}
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) {
					actionMode = null;
					position=-1;
				}
			};

	/**
	 * Show dialog box to insert data.
	 */
	private void ShowDialogBox() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		View viewForDailog = inflater.inflate(R.layout.dialog, null);

		final EditText textName = ((EditText) viewForDailog.findViewById(R.id.editName));
		final EditText textAddress = ((EditText) viewForDailog.findViewById(R.id.editAddress));

		builder.setView(viewForDailog).setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				/*
				 * Populate data to your entity class to insert.
				 * Leave id field as it is. It will auto generate id after insersion.
				 */
				MyEntityClass dataObject = new MyEntityClass();
				
				dataObject.setTitle(textName.getText().toString());
				dataObject.setSubTitle(textAddress.getText().toString());
				dataObject.setTag(new SimpleDateFormat().format(new Date()));

				//Open data base session
				Session session=manager.openSession();
				
				//Pass the entity instance to be insert.
				session.insert(dataObject);
				
				//Close session after use for safe.
				session.close();
				
				initList();

				dialog.dismiss();
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}
}
