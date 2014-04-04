package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class connPoolActivity extends Activity implements OnClickListener {
	private Bundle b;
	private EditText txtNomPool;
	private QingPoolDatasource bd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.bd = new QingPoolDatasource(this);
		this.bd.open();
		setContentView(R.layout.connpool);
		b = getIntent().getExtras();
		View btnClick1 = findViewById(R.id.btnLaConn);
		txtNomPool = (EditText) findViewById(R.id.editText1);
		btnClick1.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnConnReset) {

		}

		if (arg0.getId() == R.id.btnLaConn) {

			int idPoolSelect = bd.verifPool(txtNomPool.getText().toString());
			List<JoueurPool> joueurs = new ArrayList<JoueurPool>();
			joueurs = bd.getTousJoueurs(b.getInt("idPart"), idPoolSelect);

			if (joueurs.size() == 0) {
				if (idPoolSelect != -1) {
					// define a new Intent for the second Activity
					Intent intent = new Intent(this, listRandActivity.class);
					// start the second Activity
					b.putInt("idPoolselect", idPoolSelect);
					intent.putExtras(b);
					this.startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(),
							"Désolé aucune partie à ce nom", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Désolé vous êtes déjà inscrit dans cette partie",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		bd.open();
	}

	@Override
	public void onStop() {
		bd.close();
		super.onStop();
	}
}
