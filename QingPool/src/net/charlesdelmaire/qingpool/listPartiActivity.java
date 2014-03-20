package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class listPartiActivity extends Activity implements OnClickListener {
	List<Map<String, String>> partList = new ArrayList<Map<String, String>>();
	SimpleAdapter simpleAdpt;
	private Button btnInviter;
	ListView listParticipant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listparti);
		btnInviter = (Button) findViewById(R.id.btnInviter);
		btnInviter.setOnClickListener(this);
		initList();
		listParticipant = (ListView) findViewById(R.id.listParticipant);

		simpleAdpt = new SimpleAdapter(this, partList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomPart" }, new int[] { android.R.id.text1 });
		listParticipant.setAdapter(simpleAdpt);

		listParticipant.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Permet de changer d'activity
				Intent intent = new Intent(listPartiActivity.this,
						ProfilpartActivity.class);

				// Permet de passer un paramètre
				intent.putExtra("nom", partList.get((int) id).get("nomPart"));
				startActivity(intent);
			}
		});

	}

	// Ajouter les participants dans la liste
	private void initList() {
		partList.add(createPart("nomPart", "Charles Delmaire"));
		partList.add(createPart("nomPart", "Charles Drolet"));
		partList.add(createPart("nomPart", "Olivier Labonté"));
		partList.add(createPart("nomPart", "Jonathan Anctil"));
		partList.add(createPart("nomPart", "Michael Leclerc"));
		partList.add(createPart("nomPart", "Marie Couture"));
		partList.add(createPart("nomPart", "Tommy Tremblay"));
	}

	// Gestion de la liste
	private HashMap<String, String> createPart(String key, String name) {
		HashMap<String, String> Pool = new HashMap<String, String>();
		Pool.put(key, name);
		return Pool;
	}

	@Override
	public void onClick(View arg0) {

		// Action Bouton inviter
		if (arg0.getId() == R.id.btnInviter) {
			Intent intent = new Intent(this, EnvoieCourrielActivity.class);
			this.startActivity(intent);
		}

	}

}
