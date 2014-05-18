package net.charlesdelmaire.qingpool;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class listRandActivity extends ListActivity implements OnClickListener {
	private final String TAG = this.getClass().getSimpleName();

	// Version h�berg�e
	private final static String WEB_SERVICE_URL = "charlesdelmaire1992.appspot.com";
	private Bundle b;
	private QingPoolDatasource bd;
	private HttpClient m_ClientHttp = new DefaultHttpClient();
	private ProgressDialog m_ProgressDialog;
	ArrayList<String> m_Personnes = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Appel du layout
		setContentView(R.layout.listrand);
		getActionBar().setHomeButtonEnabled(true);
		b = getIntent().getExtras();

		// Connexion � la BD
		this.bd = new QingPoolDatasource(this);
		this.bd.open();

		// Boutons et OnClickListener
		View btnClick = findViewById(R.id.btnRege);
		btnClick.setOnClickListener(this);
		View btnClick1 = findViewById(R.id.btnAccepter);
		btnClick1.setOnClickListener(this);
		View logoClick = findViewById(R.id.imageView1);
		logoClick.setOnClickListener(this);

		if (savedInstanceState != null) {
			m_Personnes = savedInstanceState.getStringArrayList("listJoueur");
			listRandActivity.this.setListAdapter(new ArrayAdapter<String>(
					listRandActivity.this, android.R.layout.simple_list_item_1,
					m_Personnes));
		} else {

			new DownloadPersonListTask().execute((Void) null);
		}

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_liste_alea));

		tracker.send(hitParameters);

	}

	// Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// S�lections du menu
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
			alertDialog.setMessage(Html
					.fromHtml(getString(R.string.aide_list_rand)));
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

	// Passage de la liste en sortie
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArrayList("listJoueur", m_Personnes);
	}

	@Override
	public void onStop() {
		super.onStop();
		bd.close();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	// T�l�chargement de la liste al�atoire
	private class DownloadPersonListTask extends
			AsyncTask<Void, Void, ArrayList<String>> {
		Exception m_Exp;

		@Override
		protected void onPreExecute() {
			if (m_ProgressDialog == null) {
				m_ProgressDialog = new ProgressDialog(listRandActivity.this);
				m_ProgressDialog.setTitle(R.string.pd_title);
				m_ProgressDialog.setMessage(listRandActivity.this
						.getString(R.string.pd_content));
				m_ProgressDialog.setCancelable(false);
				m_ProgressDialog.setIndeterminate(true);
			}
			m_ProgressDialog.show();
		}

		// Appel du Web Service
		@Override
		protected ArrayList<String> doInBackground(Void... unused) {
			ArrayList<String> liste = null;

			try {
				URI uri = new URI("http", WEB_SERVICE_URL, "/aleatoire", null,
						null);
				HttpGet getMethod = new HttpGet(uri);
				String body = m_ClientHttp.execute(getMethod,
						new BasicResponseHandler());
				Log.i(TAG, "Re�u : " + body);
				liste = JsonParser.parseListePersonne(body);
			} catch (Exception e) {
				m_Exp = e;
			}
			return liste;
		}

		// Affichage de la liste une fois t�l�charg�e
		@Override
		protected void onPostExecute(ArrayList<String> p_Personnes) {
			if (m_ProgressDialog != null) {
				m_ProgressDialog.dismiss();
			}

			if (m_Exp == null && p_Personnes != null) {
				m_Personnes.clear();
				m_Personnes.addAll(p_Personnes);
				listRandActivity.this.setListAdapter(new ArrayAdapter<String>(
						listRandActivity.this,
						android.R.layout.simple_list_item_1, m_Personnes));
			} else {
				Log.e(TAG, "Error while fetching", m_Exp);
				Toast.makeText(listRandActivity.this,
						getString(R.string.comm_error), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;

		// Reg�n�ration de la liste al�atoire
		if (arg0.getId() == R.id.btnRege) {
			new DownloadPersonListTask().execute((Void) null);
		}

		// Accpetation de la liste et insertion dans la BD
		if (arg0.getId() == R.id.btnAccepter) {
			int idPool = b.getInt("idPoolselect");
			int idPart = b.getInt("idPart");
			int score = 0;
			for (int i = 0; i < m_Personnes.size(); i++) {
				JoueurPool unJou = new JoueurPool();
				unJou.setNomJoueur(m_Personnes.get(i).split(" ")[0] + " "
						+ m_Personnes.get(i).split(" ")[1]);
				String[] lescore = m_Personnes.get(i).split(" ");
				String lescore1 = lescore[lescore.length - 1];
				score += Integer.parseInt(lescore1.substring(0,
						lescore1.length() - 3));
				unJou.setIdPool(idPool);
				unJou.setIdPart(idPart);
				bd.createJoueur(unJou);

			}

			PartScore unScore = new PartScore();
			unScore.setIdPart(idPart);
			unScore.setIdPool(idPool);
			unScore.setScore(score);
			bd.createPartScore(unScore);

			// Message d'ajout des joueurs
			Toast.makeText(getApplicationContext(),
					getString(R.string.toast_joueur_ajoute), Toast.LENGTH_SHORT)
					.show();
			b.remove("listJoueur");

			intent = new Intent(this, principaleActivity.class);
			intent.putExtras(b);
			this.startActivity(intent);
			this.finish();
		}
	}
}
