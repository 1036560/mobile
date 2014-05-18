package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class rsltFinalActivity extends Activity {

	// Variables
	private Bundle b;
	private List<Participant> lstPart;
	private List<PartScore> lstPartScore;
	private List<Map<String, String>> partList;
	private QingPoolDatasource bd;
	String lesJoueurs;
	SimpleAdapter simpleAdpt;
	ListView lv;
	private List<JoueurPool> listJoueurs;
	List<String> listJoueur = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Appel du layout
		setContentView(R.layout.rsltfinal);

		// R�cup�ration des views
		lv = (ListView) findViewById(R.id.list);
		partList = new ArrayList<Map<String, String>>();
		b = getIntent().getExtras();
		getActionBar().setHomeButtonEnabled(true);

		// Connexion � la BD
		this.bd = new QingPoolDatasource(this);
		this.bd.open();
		lstPart = bd.getTousPart(b.getInt("idPoolSelect"));
		lstPartScore = bd.getTousPartScore(b.getInt("idPoolSelect"));
		initList();

		// Adaptateur
		simpleAdpt = new SimpleAdapter(this, partList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomPart" }, new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		// Google analytics tracker
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_rslt_pool));
		tracker.send(hitParameters);
	}

	private HashMap<String, String> createPart(String key, String name) {
		HashMap<String, String> Pool = new HashMap<String, String>();
		Pool.put(key, name);
		return Pool;
	}

	// Menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, principaleActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Initialisation de laliste
	private void initList() {
		List<String> ordre = new ArrayList<String>();
		List<String> ordre1 = new ArrayList<String>();
		;

		for (int i = 0; i < lstPart.size(); i++) {
			if (ordre.size() == 0) {
				ordre.add(lstPart.get(i).getNomPart() + " "
						+ lstPartScore.get(i).getScore());
			} else if (Integer
					.parseInt(ordre.get(ordre.size() - 1).split(" ")[2]) > lstPartScore
					.get(i).getScore()) {
				ordre.add(lstPart.get(i).getNomPart() + " "
						+ lstPartScore.get(i).getScore());
			} else {
				for (int j = 0; j < ordre.size(); j++) {
					if (Integer.parseInt(ordre.get(j).split(" ")[2]) > lstPartScore
							.get(i).getScore()) {
						ordre1.add(ordre.get(j));
					} else {
						ordre1.add(lstPart.get(i).getNomPart() + " "
								+ lstPartScore.get(i).getScore());
						ordre1.add(ordre.get(j));
					}

				}

				ordre = ordre1;
			}

		}

		for (int i = 0; i < ordre.size(); i++) {
			partList.add(createPart("nomPart", ordre.get(i)));
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

}
