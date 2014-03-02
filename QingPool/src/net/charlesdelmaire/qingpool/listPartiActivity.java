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
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class listPartiActivity extends Activity {
	List<Map<String, String>> partList = new ArrayList<Map<String, String>>();
	SimpleAdapter simpleAdpt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listparti);
		initList();

		ListView lv = (ListView) findViewById(R.id.listParticipant);

		simpleAdpt = new SimpleAdapter(this, partList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomPart" }, new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		registerForContextMenu(lv);

	}

	private void initList() {
		partList.add(createPart("nomPart", "Charles Delmaire"));
		partList.add(createPart("nomPart", "Charles Drolet"));
		partList.add(createPart("nomPart", "Olivier Labonté"));
		partList.add(createPart("nomPart", "Jonathan Anctil"));
		partList.add(createPart("nomPart", "Michael Leclerc"));
		partList.add(createPart("nomPart", "Marie Couture"));
		partList.add(createPart("nomPart", "Tommy Tremblay"));
	}

	private HashMap<String, String> createPart(String key, String name) {
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
		Intent intent = new Intent(this, ProfilpartActivity.class);
		// start the second Activity
		this.startActivity(intent);
	}

}
