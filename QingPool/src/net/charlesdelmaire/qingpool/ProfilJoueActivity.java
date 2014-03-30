<<<<<<< HEAD
package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ProfilJoueActivity extends Activity implements OnClickListener {
	TextView txtNomJoueur;
	TextView txtScore;
	TextView txtTeam;
	Bundle param;
	View btnRetourPart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiljoue);

		btnRetourPart = findViewById(R.id.btnRetPart);
		btnRetourPart.setOnClickListener(this);
		txtNomJoueur = (TextView) findViewById(R.id.nomJoueur);
		txtScore = (TextView) findViewById(R.id.score);
		txtTeam = (TextView) findViewById(R.id.equipe);

		// gestion des params
		param = getIntent().getExtras();
		txtNomJoueur.setText(txtNomJoueur.getText() + " "
				+ param.getString("nom"));
		txtScore.setText(txtScore.getText() + " " + param.getString("point")
				+ " Pts");
		txtTeam.setText(txtTeam.getText() + " " + param.getString("equipe"));
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btnRetPart) {
			onBackPressed();
		}
	}

}
=======
package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ProfilJoueActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiljoue);

		View btnClick = findViewById(R.id.btnRetPart);
		btnClick.setOnClickListener(this);
		TextView textView = (TextView) findViewById(R.id.nomJoueur);
		TextView textView1 = (TextView) findViewById(R.id.score);
		TextView textView2 = (TextView) findViewById(R.id.equipe);
		Bundle b = getIntent().getExtras();
		textView.setText(textView.getText() + " " + b.getString("nom"));
		textView1.setText(textView1.getText() + " " + b.getString("point")
				+ " Pts");
		textView2.setText(textView2.getText() + " " + b.getString("equipe"));
	}

	@Override
	public void onClick(View v) {
		// define a new Intent for the second Activity
		Intent intent = new Intent(this, ProfilpartActivity.class);
		// start the second Activity
		this.startActivity(intent);

	}

}
>>>>>>> eef06ed0b41344d3a45a0cb953d52e792e4e6217
