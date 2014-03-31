package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class EnvoieCourrielActivity extends Activity implements OnClickListener {
	private Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.envoiecourriel);

		b = getIntent().getExtras();
		// int value = b.getInt("id");
		// Toast.makeText(getApplicationContext(), Integer.toString(value),
		// Toast.LENGTH_SHORT).show();

		// value = b.getInt("idPool");
		// Toast.makeText(getApplicationContext(), Integer.toString(value),
		// Toast.LENGTH_SHORT).show();

		View btnClick = findViewById(R.id.btnEnvoieCou);
		btnClick.setOnClickListener(this);

		View btnClick1 = findViewById(R.id.retourPagePrincipale);
		btnClick1.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {

		if (v.getId() == R.id.retourPagePrincipale) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, principaleActivity.class);
			// start the second Activity
			intent.putExtras(b);
			this.startActivity(intent);
		}

		if (v.getId() == R.id.btnEnvoieCou) {
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "Contacts@Invit�s.com" });
			i.putExtra(Intent.EXTRA_SUBJECT,
					"Venez nous rejoindre sur Qing Pool");
			i.putExtra(Intent.EXTRA_TEXT,
					"Nous avons un Pool de cr�er et nous aimerions vous avoir avec nous!");
			try {
				startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this,
						"Vous n'avez pas de client courriel d'install�.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
