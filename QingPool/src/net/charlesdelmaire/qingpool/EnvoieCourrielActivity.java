package net.charlesdelmaire.qingpool;

import java.util.HashMap;

import android.app.Activity;
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
			i.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "Contacts@Invit�s.com" });
			i.putExtra(Intent.EXTRA_SUBJECT,
					"Venez nous rejoindre sur Qing Pool");
			i.putExtra(Intent.EXTRA_TEXT,
					"Nous avons un Pool de cr�er et nous aimerions vous avoir avec nous!");
			try {
				startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this,
						"Vous n'avez pas de client courriel d'install�.",
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
