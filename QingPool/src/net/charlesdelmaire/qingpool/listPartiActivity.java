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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class listPartiActivity extends Activity implements OnClickListener {
	private QingPoolDatasource bd;
	private List<Participant> lstPart;
	List<Map<String, String>> partList = new ArrayList<Map<String, String>>();
	SimpleAdapter simpleAdpt;
	private Button button1;
	private Button btnRsltFinal;
	TextView textView;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listparti);
		button1 = (Button) findViewById(R.id.btnInviter);
		button1.setOnClickListener(this);
		btnRsltFinal = (Button) findViewById(R.id.btnRsltFinal);
		btnRsltFinal.setOnClickListener(this);
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

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_liste_part));

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
			alertDialog.setMessage(getString(R.string.aide_list_part));
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

		for (int i = 0; i < lstPart.size(); i++) {
			partList.add(createPart("nomPart", lstPart.get(i).getNomPart()));
		}
	}

	private HashMap<String, String> createPart(String key, String name) {
		HashMap<String, String> Pool = new HashMap<String, String>();
		Pool.put(key, name);
		return Pool;
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnInviter) {

			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(
					Intent.EXTRA_EMAIL,
					new String[] { getString(R.string.courriel_contacts_invite) });
			i.putExtra(Intent.EXTRA_SUBJECT,
					getString(R.string.courriel_rejoindre));
			i.putExtra(Intent.EXTRA_TEXT, getString(R.string.courriel_cree));
			try {
				startActivity(Intent.createChooser(i,
						getString(R.string.courriel_envoie)));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this,
						getString(R.string.toast_courriel_inexistant),
						Toast.LENGTH_SHORT).show();
			}

		}
		if (arg0.getId() == R.id.btnRsltFinal) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, rsltFinalActivity.class);

			// start the second Activity
			this.startActivity(intent);
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