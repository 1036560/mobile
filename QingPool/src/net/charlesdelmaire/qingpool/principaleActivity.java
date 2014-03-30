<<<<<<< HEAD
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
=======
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

		View btnClick = findViewById(R.id.btnStartPool);
		btnClick.setOnClickListener(this);

		View btnClick1 = findViewById(R.id.btnConnPool);
		btnClick1.setOnClickListener(this);

		View btnClick2 = findViewById(R.id.btnViewPool);
		btnClick2.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btnConnPool) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, connPoolActivity.class);
			// start the second Activity
			this.startActivity(intent);
		}
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnStartPool) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, nouveauPoolActivity.class);
			// start the second Activity
			this.startActivity(intent);
		}

		if (v.getId() == R.id.btnViewPool) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, listPoolActivity.class);
			// start the second Activity
			this.startActivity(intent);
		}

	}

}
>>>>>>> eef06ed0b41344d3a45a0cb953d52e792e4e6217
