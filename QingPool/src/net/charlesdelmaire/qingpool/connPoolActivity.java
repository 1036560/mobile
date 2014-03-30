package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class connPoolActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connpool);

		View btnClick1 = findViewById(R.id.btnLaConn);
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
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, listRandActivity.class);
			// start the second Activity
			this.startActivity(intent);
		}
	}
}
