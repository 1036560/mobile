package net.charlesdelmaire.qingpool;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class nouveauPoolActivity extends Activity implements OnClickListener {
	private QingPoolDatasource bd;
	EditText nomPoolEdit;
	EditText motPasse1;
	EditText motPasse2;
	EditText txtNbPart;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouveaupool);
		b = getIntent().getExtras();

		this.bd = new QingPoolDatasource(this);
		this.bd.open();

		View btnClick = findViewById(R.id.btnCreerPool);
		nomPoolEdit = (EditText) findViewById(R.id.editText1);
		motPasse1 = (EditText) findViewById(R.id.txtMotPasse);
		motPasse2 = (EditText) findViewById(R.id.txtMotPasse2);
		txtNbPart = (EditText) findViewById(R.id.txtNbPart);
		View resetClick = findViewById(R.id.btnPoolReset);
		View logoClick = findViewById(R.id.imageView1);
		resetClick.setOnClickListener(this);
		logoClick.setOnClickListener(this);
		

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_nouveau_pool));

		tracker.send(hitParameters);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.idRetour:
			intent = new Intent(this, principaleActivity.class);
			intent.putExtras(b);
			this.startActivity(intent);
			break;
		case R.id.deconnexion:
			intent = new Intent(this, connexionActivity.class);
			b.putInt("deconnexion", 1);
			intent.putExtras(b);
			this.startActivity(intent);
			break;
		case R.id.aide:
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getString(R.string.menu_aide));
			alertDialog.setMessage(Html.fromHtml(getString(R.string.aide_nouv_pool)));
			alertDialog.setButton(getString(R.string.fermer),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}); // Set the Icon for the Dialog
			alertDialog.setIcon(R.drawable.aide);
			alertDialog.show();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		this.bd.open();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.bd.close();
		EasyTracker.getInstance(this).activityStop(this);
	}

	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnCreerPool) {
			if (motPasse1.getText().toString()
					.equals(motPasse2.getText().toString())
					&& !motPasse1.getText().toString().isEmpty()) {
				Pool unPool = new Pool();
				int pool_id = bd.getPoolCompte();
				String nomPool = nomPoolEdit.getText().toString();

				int idPool = bd.verifPool(nomPool);

				if (idPool == -1) {

					String mdp = motPasse1.getText().toString();

					if (!txtNbPart.getText().toString().isEmpty()) {
						int nbPart = Integer.parseInt(txtNbPart.getText()
								.toString());
						unPool.setIdPool(pool_id);
						unPool.setNomPool(nomPool);
						unPool.setMotDePasse(mdp);
						unPool.setNbMaxPart(nbPart);
						b.putInt("idPoolselect", bd.createPool(unPool));
						// define a new Intent for the second Activity
						Intent intent = new Intent(this, listRandActivity.class);
						// start the second Activity
						intent.putExtras(b);
						this.startActivity(intent);
						this.finish();
					} else {
						Toast.makeText(getApplicationContext(),
								getString(R.string.toast_pool_maximum),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.toast_pool_meme_nom),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.toast_pool_mdp), Toast.LENGTH_SHORT)
						.show();
			}
		}
		if (arg0.getId() == R.id.imageView1) {
			Intent intent = null;
			intent = new Intent(this, principaleActivity.class);
			intent.putExtras(b);
			this.startActivity(intent);
		}
		
		if (arg0.getId() == R.id.btnPoolReset){
			nomPoolEdit.setText("");
			motPasse1.setText("");
			motPasse2.setText("");
			txtNbPart.setText("");
			nomPoolEdit.setFocusableInTouchMode(true);
			nomPoolEdit.requestFocus();
		}

	}
}