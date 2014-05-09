package net.charlesdelmaire.qingpool;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class principaleActivity extends Activity implements OnClickListener {

	private int idPart;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pageprincipale);
		getActionBar().setHomeButtonEnabled(true);
		b = getIntent().getExtras();

		View btnClick = findViewById(R.id.btnStartPool);
		btnClick.setOnClickListener(this);

		View btnClick1 = findViewById(R.id.btnConnPool);
		btnClick1.setOnClickListener(this);

		View btnClick2 = findViewById(R.id.btnViewPool);
		btnClick2.setOnClickListener(this);

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_principale));

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
		case R.id.aide:

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getString(R.string.menu_aide));
			alertDialog.setMessage(Html.fromHtml(getString(R.string.aide_principale)));
			alertDialog.setButton(getString(R.string.fermer),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}); // Set the Icon for the Dialog
			alertDialog.setIcon(R.drawable.aide);
			alertDialog.show();

			break;
		case R.id.deconnexion:
			intent = new Intent(this, connexionActivity.class);
			b.putInt("deconnexion", 1);
			intent.putExtras(b);
			this.startActivity(intent);
			break;
		case android.R.id.home:            
	         intent = new Intent(this, principaleActivity.class);   
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent); 
	         break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btnConnPool) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, connPoolActivity.class);
			// start the second Activity
			intent.putExtras(b);
			this.startActivity(intent);
		}
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnStartPool) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, nouveauPoolActivity.class);
			// start the second Activity
			intent.putExtras(b);
			this.startActivity(intent);
		}

		if (v.getId() == R.id.btnViewPool) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, listPoolActivity.class);
			// start the second Activity
			intent.putExtras(b);
			this.startActivity(intent);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

}
