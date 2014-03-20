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
