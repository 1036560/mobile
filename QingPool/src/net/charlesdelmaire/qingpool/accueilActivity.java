package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class accueilActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil);

		View btnClick = findViewById(R.id.btnConnexion);
		btnClick.setOnClickListener(this);
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
}
