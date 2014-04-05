package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class nouveauPoolActivity extends Activity implements OnClickListener {
	private QingPoolDatasource bd;
	EditText nomPoolEdit;
	EditText motPasse1;
	EditText motPasse2;
	EditText txtNbPart;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouveaupool);
		b = getIntent().getExtras();

		this.bd = new QingPoolDatasource(this);
		this.bd.open();

		View btnClick = findViewById(R.id.btnCreerPool);
		nomPoolEdit = (EditText) findViewById(R.id.editText1);
		motPasse1 = (EditText) findViewById(R.id.txtMotPasse);
		motPasse2 = (EditText) findViewById(R.id.txtMotPasse2);
		txtNbPart = (EditText) findViewById(R.id.txtNbPart);
		btnClick.setOnClickListener(this);

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
			if (motPasse1.getText().toString()
					.equals(motPasse2.getText().toString())
					&& !motPasse1.getText().toString().isEmpty()) {
				Pool unPool = new Pool();
				int pool_id = bd.getPoolCompte();
				String nomPool = nomPoolEdit.getText().toString();

				int idPool = bd.verifPool(nomPool);

				if (idPool == -1) {

					String mdp = motPasse1.getText().toString();

					if (!txtNbPart.getText().toString().isEmpty()) {
						int nbPart = Integer.parseInt(txtNbPart.getText()
								.toString());
						unPool.setIdPool(pool_id);
						unPool.setNomPool(nomPool);
						unPool.setMotDePasse(mdp);
						unPool.setNbMaxPart(nbPart);
						bd.createPool(unPool);
						// define a new Intent for the second Activity
						Intent intent = new Intent(this,
								EnvoieCourrielActivity.class);
						// start the second Activity
						intent.putExtras(b);
						this.startActivity(intent);
					} else {
						Toast.makeText(
								getApplicationContext(),
								"Vous devez mettre un nombre de participant minimum",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Désolé ce pool éxiste déjà! Veuillez prendre un notre nom! ",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Les mots de passe sont différent!", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}
}