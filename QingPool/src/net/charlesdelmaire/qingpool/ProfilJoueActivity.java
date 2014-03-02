package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ProfilJoueActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiljoue);

		View btnClick = findViewById(R.id.btnRetPart);
		btnClick.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// define a new Intent for the second Activity
		Intent intent = new Intent(this, ProfilpartActivity.class);
		// start the second Activity
		this.startActivity(intent);

	}

}
