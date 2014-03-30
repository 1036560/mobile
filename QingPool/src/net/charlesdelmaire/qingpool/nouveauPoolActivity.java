package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class nouveauPoolActivity extends Activity implements OnClickListener {
	private QingPoolDatasource bd;
	EditText nomPoolEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouveaupool);

		this.bd = new QingPoolDatasource(this);
		this.bd.open();

		View btnClick = findViewById(R.id.btnCreerPool);
		nomPoolEdit = (EditText) findViewById(R.id.editText1);
		btnClick.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Ouverture de la connexion à la BD au démarrage de l'activité.
	 */
	@Override
	protected void onStart() {
		this.bd.open();
		super.onStart();
	}

	@Override
	protected void onStop() {
		this.bd.close();
		super.onStop();
	}

	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnCreerPool) {
			Pool unPool = new Pool();
			int pool_id = bd.getPoolCompte();
			String nomPool = nomPoolEdit.getText().toString();
			unPool.idPool = pool_id;
			unPool.nomPool = nomPool.toString();
			bd.createPool(unPool);
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, EnvoieCourrielActivity.class);
			// start the second Activity
			this.startActivity(intent);

		}

	}

}