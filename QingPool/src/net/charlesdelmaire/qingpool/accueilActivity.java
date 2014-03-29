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

	private UtilitaireBD bd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil);
		
		bd = new UtilitaireBD(this);
		
		
		
		Participant part1 = new Participant(1, "Chuck Testa");
		Participant part2 = new Participant(2, "Marie Quatre-Poches");
		Participant part3 = new Participant(3, "Joe Blo");
		
		
		
		/*Log.d("Part count", "Part count : " + bd.getPartCompte());
		
		Pool pool1 = new Pool(1, "Pool1Nom", part1.getIdPart());
		Pool pool2 = new Pool(2, "Pool2Nom", part2.getIdPart());
		Pool pool3 = new Pool(3, "Pool3Nom", part3.getIdPart());
		
		long pool1_id = bd.createPool(pool1);
		long pool2_id = bd.createPool(pool2);
		long pool3_id = bd.createPool(pool3);
		
		Log.e("Pool count", "Pool count: " + bd.getTousPool().size());*/
		
		bd.close();

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
