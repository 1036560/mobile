package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class nouveauPoolActivity extends Activity implements OnClickListener {
	private QingPoolDatasource bd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouveaupool);
		
		bd = new QingPoolDatasource(this);
		bd.open();
		
		View btnClick = findViewById(R.id.btnCreerPool);
		btnClick.setOnClickListener(this);

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnCreerPool) {
			Pool unPool = new Pool();
			int pool_id = bd.getPoolCompte() + 1;
			CharSequence nomPool = getText(R.id.editText1);
			unPool.idPool = pool_id;			
			unPool.nomPool = nomPool.toString();
			bd.createPool(unPool);
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, EnvoieCourrielActivity.class);
			// start the second Activity			
			this.startActivity(intent);
			bd.close();
		}

	}

}