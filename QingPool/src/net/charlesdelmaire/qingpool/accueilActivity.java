<<<<<<< HEAD
package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class accueilActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil);

		View btnConnexion = findViewById(R.id.btnConnexion);
		btnConnexion.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {

		// Action bouton connexion
		if (arg0.getId() == R.id.btnConnexion) {
			Intent intent = new Intent(this, connexionActivity.class);
			this.startActivity(intent);
		}

	}

}
=======
package net.charlesdelmaire.qingpool;

import net.charlesdelmaire.*;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
>>>>>>> eef06ed0b41344d3a45a0cb953d52e792e4e6217
