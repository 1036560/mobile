package net.charlesdelmaire.qingpool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilpartActivity extends Activity implements OnClickListener {

	final Activity activity = this;
	private Button button;
	private Button button1;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.profilpart);

		button = (Button) findViewById(R.id.btnVoirJoueur);
		button.setOnClickListener(this);
		button1 = (Button) findViewById(R.id.btnInviter);
		button1.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == R.id.btnInviter) {
			// define a new Intent for the second Activity
			Intent intent = new Intent(this, EnvoieCourrielActivity.class);
			// start the second Activity
			this.startActivity(intent);
		}

		if (arg0.getId() == R.id.btnVoirJoueur) {
			final Dialog dialog = new Dialog(activity);
			dialog.setContentView(R.layout.dialog_custom);
			dialog.setTitle("Liste des joueurs");

			// set the custom dialog components - text, image and button
			TextView text = (TextView) dialog.findViewById(R.id.text);
			ImageView image = (ImageView) dialog.findViewById(R.id.image);
			TextView text1 = (TextView) dialog.findViewById(R.id.text1);
			ImageView image1 = (ImageView) dialog.findViewById(R.id.image1);
			TextView text2 = (TextView) dialog.findViewById(R.id.text2);
			ImageView image2 = (ImageView) dialog.findViewById(R.id.image2);

			image.setImageResource(R.drawable.hockey_joueur);
			text.setText("nom_joueur, (score)");
			image1.setImageResource(R.drawable.hockey_joueur);
			text1.setText("nom_joueur1, (score1)");
			image2.setImageResource(R.drawable.hockey_joueur);
			text2.setText("nom_joueur2, (score2)");

			Button dialogButton = (Button) dialog.findViewById(R.id.btnFermer);
			Button dialogButton1 = (Button) dialog
					.findViewById(R.id.btnVoirProfilJoueur);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialogButton1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(v.getContext(),
							ProfilJoueActivity.class);
					v.getContext().startActivity(i);
				}

			});

			dialog.show();

		}

	}
}
