package net.charlesdelmaire.qingpool;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class EnvoieCourrielActivity extends Activity implements OnClickListener {
	private Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.envoiecourriel);

		b = getIntent().getExtras();

		View btnClick = findViewById(R.id.btnEnvoieCou);
		btnClick.setOnClickListener(this);

		View btnClick1 = findViewById(R.id.retourPagePrincipale);
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
		case R.id.gestionCompte:
			intent = new Intent(this, connexionActivity.class);
			intent.putExtras(b);
			this.startActivity(intent);
			break;
		case R.id.aide:

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getString(R.string.menu_aide));
			alertDialog.setMessage(getString(R.string.aide_envoie_courriel));
			alertDialog.setButton(getString(R.string.fermer), new DialogInterface.OnClickListener() {
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

	public void onClick(View v) {

		if (v.getId() == R.id.retourPagePrincipale) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, principaleActivity.class);
			// start the second Activity
			intent.putExtras(b);
			this.startActivity(intent);
		}

		if (v.getId() == R.id.btnEnvoieCou) {
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
