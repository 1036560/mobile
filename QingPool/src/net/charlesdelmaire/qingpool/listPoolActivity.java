package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class listPoolActivity extends Activity implements OnClickListener{
	private QingPoolDatasource bd;
	private List<Pool> lstPool;
	private ArrayAdapter<Pool> adpt;
	List<Map<String, String>> poolList = new ArrayList<Map<String, String>>();
	SimpleAdapter simpleAdpt;
	ListView lv;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listpool);
		this.bd = new QingPoolDatasource(this);
		this.bd.open();
		b = getIntent().getExtras();

		lstPool = bd.getTousPool(b.getInt("idPart"));
		initList();

		lv = (ListView) findViewById(R.id.listPool);

		simpleAdpt = new SimpleAdapter(this, poolList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomPool" }, new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		registerForContextMenu(lv);
		
		View logoClick = findViewById(R.id.imageView1);
		logoClick.setOnClickListener(this);

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_liste_pool));

		tracker.send(hitParameters);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.idRetour:
			intent = new Intent(this, principaleActivity.class);
			intent.putExtras(b);
			this.startActivity(intent);
			break;
		case R.id.deconnexion:
			intent = new Intent(this, connexionActivity.class);
			b.putInt("deconnexion", 1);
			intent.putExtras(b);
			this.startActivity(intent);
			break;
		case R.id.aide:
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getString(R.string.menu_aide));
			alertDialog.setMessage(Html.fromHtml(getString(R.string.aide_list_pool)));
			alertDialog.setButton(getString(R.string.fermer),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}); // Set the Icon for the Dialog
			alertDialog.setIcon(R.drawable.aide);
			alertDialog.show();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initList() {

		for (int i = 0; i < lstPool.size(); i++) {
			poolList.add(createPool("nomPool", lstPool.get(i).getNomPool()));
		}
	}

	private HashMap<String, String> createPool(String key, String name) {
		HashMap<String, String> Pool = new HashMap<String, String>();
		Pool.put(key, name);
		return Pool;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

		// We know that each row in the adapter is a Map
		HashMap map = (HashMap) simpleAdpt.getItem(aInfo.position);
		menu.setHeaderTitle(getString(R.string.popup_nom) + map.get("nomPool"));
		menu.add(1, 1, 1, getString(R.string.popup_liste));
		menu.add(1, 2, 2, getString(R.string.popup_quitter));

	}

	public boolean onContextItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		String nomPool = lv.getItemAtPosition(info.position).toString()
				.split("=")[1];
		nomPool = nomPool.substring(0,
				(nomPool.length() >= 1) ? nomPool.length() - 1 : 0);
		int idPoolSelect = bd.verifPool(nomPool);
		b.putInt("idPoolSelect", idPoolSelect);
		if (itemId == 1) {
			Intent intent = new Intent(this, listPartiActivity.class);
			// start the second Activity
			intent.putExtras(b);
			this.startActivity(intent);

		} else if (itemId == 2) {
			bd.deletePartPool(idPoolSelect, b.getInt("idPart"));
			poolList.clear();
			simpleAdpt.notifyDataSetChanged();
			lstPool = bd.getTousPool(b.getInt("idPart"));
			initList();

		}
		return true;
	}
	
	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.imageView1) {
			Intent intent = null;
			intent = new Intent(this, principaleActivity.class);
			intent.putExtras(b);
			this.startActivity(intent);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		bd.open();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		bd.close();
		EasyTracker.getInstance(this).activityStop(this);
	}
}
