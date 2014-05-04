package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_conn_pool));

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
			alertDialog.setMessage(getString(R.string.aide_conn_pool));
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
							this.finish();

						} else {
							Toast.makeText(getApplicationContext(),
									getString(R.string.toast_mauvais_mdp),
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								getString(R.string.toast_nbr_max_atteint),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.toast_pool_non),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.toast_pool_deja), Toast.LENGTH_LONG)
						.show();
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
