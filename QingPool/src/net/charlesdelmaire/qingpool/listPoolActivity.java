package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class listPoolActivity extends Activity {
	List<Map<String, String>> poolList = new ArrayList<Map<String, String>>();
	SimpleAdapter simpleAdpt;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listpool);

		initList();

		lv = (ListView) findViewById(R.id.listPool);

		simpleAdpt = new SimpleAdapter(this, poolList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomPool" }, new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		registerForContextMenu(lv);

	}

	private void initList() {
		poolList.add(createPool("nomPool", "Pool - Famille"));
		poolList.add(createPool("nomPool", "Le Pool d'école"));
		poolList.add(createPool("nomPool", "Pool Cégep"));
		poolList.add(createPool("nomPool", "Les meilleurs du pool"));
		poolList.add(createPool("nomPool", "Pool 2014"));
		poolList.add(createPool("nomPool", "Pool étudiant"));
		poolList.add(createPool("nomPool", "Les Pools"));
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
		menu.setHeaderTitle("Les Options pour le pool: " + map.get("nomPool"));
		menu.add(1, 1, 1, "Liste de participant");
		menu.add(1, 2, 2, "Quitter un pool");

	}

	public boolean onContextItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		// return true;

		if (itemId == 1) {
			Intent intent = new Intent(this, listPartiActivity.class);
			// start the second Activity

			this.startActivity(intent);
		} else if (itemId == 2) {

		}
		return true;
	}
}
