package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class principaleActivity extends Activity implements OnClickListener {

	private int idPart;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pageprincipale);

		b = getIntent().getExtras();

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
			intent.putExtras(b);
			this.startActivity(intent);
		}
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnStartPool) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, nouveauPoolActivity.class);
			// start the second Activity
			intent.putExtras(b);
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
