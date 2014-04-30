package net.charlesdelmaire.qingpool;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class accueilActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil);

		View btnClick = findViewById(R.id.btnConnexion);
		btnClick.setOnClickListener(this);

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, getString(R.string.screen_accueil));

		tracker.send(hitParameters);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnConnexion) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, connexionActivity.class);
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
