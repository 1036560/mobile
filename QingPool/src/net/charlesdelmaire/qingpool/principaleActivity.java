package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class principaleActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pageprincipale);

		View btnStartPool = findViewById(R.id.btnStartPool);
		btnStartPool.setOnClickListener(this);

		View btnConnPool = findViewById(R.id.btnConnPool);
		btnConnPool.setOnClickListener(this);

		View btnViewPool = findViewById(R.id.btnViewPool);
		btnViewPool.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		// Action bouton connexion à un pool
		if (v.getId() == R.id.btnConnPool) {
			Intent intent = new Intent(this, connPoolActivity.class);
			this.startActivity(intent);
		}
		// Action bouton demarrer un pool
		if (v.getId() == R.id.btnStartPool) {
			Intent intent = new Intent(this, nouveauPoolActivity.class);
			this.startActivity(intent);
		}
		// Action bouton voir les pool d'un participant
		if (v.getId() == R.id.btnViewPool) {
			Intent intent = new Intent(this, listPoolActivity.class);
			this.startActivity(intent);
		}

	}

}
