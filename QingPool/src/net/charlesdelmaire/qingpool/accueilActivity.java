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
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Appel du layout
		setContentView(R.layout.accueil);
		
		//Bouton et OnClickListener
		View btnClick = findViewById(R.id.btnConnexion);
		btnClick.setOnClickListener(this);

		//Google analytics tracker
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_accueil));

		tracker.send(hitParameters);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnConnexion) {	
			
			//D�marrage de la page
			Intent intent = new Intent(this, connexionActivity.class);
			Bundle b = new Bundle();
			b.putInt("deconnexion", 0);
			intent.putExtras(b);
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
