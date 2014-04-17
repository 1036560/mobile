package net.charlesdelmaire.qingpool;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class ProfilJoueActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiljoue);

		View btnClick = findViewById(R.id.btnRetPart);
		btnClick.setOnClickListener(this);
		TextView textView = (TextView) findViewById(R.id.nomJoueur);
		TextView textView1 = (TextView) findViewById(R.id.score);
		TextView textView2 = (TextView) findViewById(R.id.equipe);
		Bundle b = getIntent().getExtras();
		textView.setText(textView.getText() + " " + b.getString("nom"));
		textView1.setText(textView1.getText() + " " + b.getString("point")
				+ " Pts");
		textView2.setText(textView2.getText() + " " + b.getString("equipe"));

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Profil Joueur");

		tracker.send(hitParameters);
	}

	@Override
	public void onClick(View v) {
		this.finish();
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
