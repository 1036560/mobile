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
import android.widget.TextView;

public class listPartiActivity extends Activity implements OnClickListener {
	private QingPoolDatasource bd;
	private List<Participant> lstPart;
	List<Map<String, String>> partList = new ArrayList<Map<String, String>>();
	SimpleAdapter simpleAdpt;
	private Button button1;
	TextView textView;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listparti);
		button1 = (Button) findViewById(R.id.btnInviter);
		button1.setOnClickListener(this);
		b = getIntent().getExtras();
		this.bd = new QingPoolDatasource(this);
		this.bd.open();

		lstPart = bd.getTousPart(b.getInt("idPoolSelect"));

		initList();
		textView = (TextView) findViewById(R.id.nomPart);
		ListView lv = (ListView) findViewById(R.id.listParticipant);

		simpleAdpt = new SimpleAdapter(this, partList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomPart" }, new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(listPartiActivity.this,
						ProfilpartActivity.class);

				String nomPart = partList.get((int) id).get("nomPart");
				int idPart = -1;

				for (int i = 0; i < lstPart.size(); i++) {
					if (nomPart.equals(lstPart.get(i).getNomPart()))
						idPart = lstPart.get(i).getIdPart();
				}

				intent.putExtra("nomPart", nomPart);
				intent.putExtra("idPartSelect", idPart);
				intent.putExtras(b);
				startActivity(intent);
			}
		});

	}

	private void initList() {

		for (int i = 0; i < lstPart.size(); i++) {
			partList.add(createPart("nomPart", lstPart.get(i).getNomPart()));
		}
		/*
		 * partList.add(createPart("nomPart", "Charles Delmaire"));
		 * partList.add(createPart("nomPart", "Charles Drolet"));
		 * partList.add(createPart("nomPart", "Olivier Labonté"));
		 * partList.add(createPart("nomPart", "Jonathan Anctil"));
		 * partList.add(createPart("nomPart", "Michael Leclerc"));
		 * partList.add(createPart("nomPart", "Marie Couture"));
		 * partList.add(createPart("nomPart", "Tommy Tremblay"));
		 */
	}

	private HashMap<String, String> createPart(String key, String name) {
		HashMap<String, String> Pool = new HashMap<String, String>();
		Pool.put(key, name);
		return Pool;
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnInviter) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, EnvoieCourrielActivity.class);

			// start the second Activity
			this.startActivity(intent);
		}

	}

}
