package net.charlesdelmaire.qingpool;

import android.app.Activity;
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
		this.finish();

	}
}
