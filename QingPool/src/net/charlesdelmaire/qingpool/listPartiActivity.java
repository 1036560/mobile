package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class listPartiActivity extends Activity implements OnClickListener {
	
	//Variables
	private QingPoolDatasource bd;
	private List<Participant> lstPart;
	List<Map<String, String>> partList = new ArrayList<Map<String, String>>();
	SimpleAdapter simpleAdpt;
	private Button button1;
	private Button btnRsltFinal;
	TextView textView;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Appel du layout
		setContentView(R.layout.listparti);
		
		//Bouton et OnClickListener
		button1 = (Button) findViewById(R.id.btnInviter);
		button1.setOnClickListener(this);
		btnRsltFinal = (Button) findViewById(R.id.btnRsltFinal);
		btnRsltFinal.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.nomPart);
		ListView lv = (ListView) findViewById(R.id.listParticipant);
		b = getIntent().getExtras();
		getActionBar().setHomeButtonEnabled(true);
		this.bd = new QingPoolDatasource(this);
		this.bd.open();

		//R�cup�ration de la liste des participants d'un pool
		lstPart = bd.getTousPart(b.getInt("idPoolSelect"));

		initList();
		
		//Adaptateur
		simpleAdpt = new SimpleAdapter(this, partList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomPart" }, new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		//Ajout de l'attribut cliquable de la liste
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//D�marrage de la nouvelle activit� avec les variables
				Intent intent = new Intent(listPartiActivity.this,
  						ProfilpartActivity.class);
 				String message = partList.get((int) id).get("nomPart");
 				intent.putExtra("nom", message);
 
 				String nomPart = partList.get((int) id).get("nomPart");
 				int idPart = -1;
 
 				for (int i = 0; i < lstPart.size(); i++) {
 					if (nomPart.equals(lstPart.get(i).getNomPart()))
 						idPart = lstPart.get(i).getIdPart();
 				}
 
 				intent.putExtra("idPartSelect", idPart);
 				intent.putExtras(b);
  				startActivity(intent);
			}
		});

		//Google analytics tracker
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Liste Participant");

		tracker.send(hitParameters);

	}

	//Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//S�lection du menu
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
			alertDialog.setMessage(Html.fromHtml(getString(R.string.aide_list_part)));
			alertDialog.setButton(getString(R.string.fermer),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialog.setIcon(R.drawable.aide);
			alertDialog.show();
			break;
		case android.R.id.home:            
	         intent = new Intent(this, principaleActivity.class);   
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent); 
	         break;
		}

		return super.onOptionsItemSelected(item);
	}

	//Initialisation de la liste
	private void initList() {
		for (int i = 0; i < lstPart.size(); i++) {
			partList.add(createPart("nomPart", lstPart.get(i).getNomPart()));
		}
	}

	private HashMap<String, String> createPart(String key, String name) {
		HashMap<String, String> Pool = new HashMap<String, String>();
		Pool.put(key, name);
		return Pool;
	}

	//Appel des services de l'appareil pour envoyer un courriel
	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btnInviter) {

			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(
					Intent.EXTRA_EMAIL,
					new String[] { getString(R.string.courriel_contacts_invite) });
			i.putExtra(Intent.EXTRA_SUBJECT,
					getString(R.string.courriel_rejoindre));
			i.putExtra(Intent.EXTRA_TEXT, getString(R.string.courriel_cree));
			try {
				startActivity(Intent.createChooser(i,
						getString(R.string.courriel_envoie)));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this,
						getString(R.string.toast_courriel_inexistant),
						Toast.LENGTH_SHORT).show();
			}

		}
		
		//Appel de la nouvelle activit�
		if (arg0.getId() == R.id.btnRsltFinal) {			
			Intent intent = new Intent(this, rsltFinalActivity.class);
			intent.putExtras(b);			
			this.startActivity(intent);
		}	

	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

}
