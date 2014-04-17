package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class connPoolActivity extends Activity implements OnClickListener {
	private Bundle b;
	private EditText txtNomPool;
	private EditText txtMotDePasse;
	private QingPoolDatasource bd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.bd = new QingPoolDatasource(this);
		this.bd.open();
		setContentView(R.layout.connpool);
		b = getIntent().getExtras();
		View btnClick1 = findViewById(R.id.btnLaConn);
		txtNomPool = (EditText) findViewById(R.id.editText1);
		txtMotDePasse = (EditText) findViewById(R.id.txtMotDePasse);
		btnClick1.setOnClickListener(this);

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Connexion Pool");

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
			break;
		case R.id.gestionCompte:
			intent = new Intent(this, connexionActivity.class);
			break;
		}
		intent.putExtras(b);
		this.startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnConnReset) {

		}

		if (arg0.getId() == R.id.btnLaConn) {

			int idPoolSelect = bd.verifPool(txtNomPool.getText().toString());

			int nbPart = bd.getJoueurCompte(idPoolSelect);

			List<JoueurPool> joueurs = new ArrayList<JoueurPool>();
			joueurs = bd.getTousJoueurs(b.getInt("idPart"), idPoolSelect);

			if (joueurs.size() == 0) {
				if (idPoolSelect != -1) {
					Pool unPool = bd.getPool(idPoolSelect);

					if (nbPart < unPool.getNbMaxPart()) {
						String leMotDePasse = txtMotDePasse.getText()
								.toString();
						if (leMotDePasse.equals(unPool.getMotDePasse())) {
							// define a new Intent for the second Activity
							Intent intent = new Intent(this,
									listRandActivity.class);
							// start the second Activity
							b.putInt("idPoolselect", idPoolSelect);
							intent.putExtras(b);
							this.startActivity(intent);

						} else {
							Toast.makeText(getApplicationContext(),
									"Mauvais mot de passe", Toast.LENGTH_LONG)
									.show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"La limite de joueur a été atteint",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Désolé aucune partie à ce nom", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Désolé vous êtes déjà inscrit dans cette partie",
						Toast.LENGTH_LONG).show();
			}

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
