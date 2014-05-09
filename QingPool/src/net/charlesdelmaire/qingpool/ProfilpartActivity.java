package net.charlesdelmaire.qingpool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class ProfilpartActivity extends Activity implements OnClickListener {

	private ProgressDialog m_ProgressDialog;
	List<Map<String, String>> partList = new ArrayList<Map<String, String>>();
	SimpleAdapter simpleAdpt;
	String lesJoueurs;
	// TextView agePart;
	TextView textView;
	TextView textView1;
	TextView textViewEmpty;
	ImageView bmImage;
	Bundle b;
	private QingPoolDatasource bd;
	private List<JoueurPool> listJoueurs;
	List<String> listJoueur = new ArrayList<String>();
	Participant participantSelect = null;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.profilpart);
		ListView lv = (ListView) findViewById(R.id.list);
		this.bd = new QingPoolDatasource(this);
		this.bd.open();
		textView = (TextView) findViewById(R.id.scorePart);
		textView1 = (TextView) findViewById(R.id.nomPart);
		textViewEmpty = (TextView) findViewById(R.id.empty);
		View logoClick = findViewById(R.id.imagePool);
		logoClick.setOnClickListener(this);
		getActionBar().setHomeButtonEnabled(true);
		b = getIntent().getExtras();

		textView1.setText(textView1.getText() + " " + b.getString("nom"));
		simpleAdpt = new SimpleAdapter(this, partList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomJoueur" }, new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		registerForContextMenu(lv);
		if (savedInstanceState != null) {
			lesJoueurs = savedInstanceState.getString("listJoueur");
			String str[] = lesJoueurs.split(";");
			int score = 0;
			for (int i = 0; i < str.length; i++) {
				String str1[] = str[i].split("/");
				score += Integer.parseInt(str1[2]);
				partList.add(createJoueur("nomJoueur", str1[0]));
			}
			textView.setText(textView.getText() + " " + Integer.toString(score));
			simpleAdpt.notifyDataSetChanged();
		} else {

			participantSelect = bd.getParticipant(b.getInt("idPartSelect"));
			new HttpAsyncTask()
					.execute("http://charlesdelmaire1992.appspot.com/joueur?nom=");

		}
		new DownloadImageTask((ImageView) findViewById(R.id.imagePart))
				.execute(participantSelect.getImgPart().substring(0,
						participantSelect.getImgPart().length() - 2)
						+ "300");

		// agePart.setText(agePart.getText()
		// + participantSelect.getLang().toUpperCase());
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Profil Participant");

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
			alertDialog.setMessage(getString(R.string.aide_prof_part));
			alertDialog.setButton(getString(R.string.fermer),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}); // Set the Icon for the Dialog
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("listJoueur", lesJoueurs);
	}

	@Override
	public void onStart() {
		super.onStart();
		bd.open();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
		bd.close();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.imageView1) {
			Intent intent = null;
			intent = new Intent(this, principaleActivity.class);
			intent.putExtras(b);
			this.startActivity(intent);
		}
	}

	public String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		String result2 = "";
		try {

			ArrayList<String> liste = null;

			for (Iterator<String> i = listJoueur.iterator(); i.hasNext();) {
				String item = i.next();

				// create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// make GET request to the given URL
				HttpResponse httpResponse = httpclient.execute(new HttpGet(url
						.concat(item)));

				// receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();

				// convert inputstream to string
				if (inputStream != null)
					result2 = convertInputStreamToString(inputStream);
				else
					result2 = "Did not work!";

				// result += result2;
				liste = JsonParser.unePersonne(result2);
				result += liste.get(0) + ";";
			}

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

		// We know that each row in the adapter is a Map
		HashMap map = (HashMap) simpleAdpt.getItem(aInfo.position);
		Bundle b = new Bundle();
		Intent intent = new Intent(this, ProfilJoueActivity.class);
		String str[] = lesJoueurs.split(";");

		for (int i = 0; i < str.length; i++) {
			String str1[] = str[i].split("/");

			if (str1[0].equals(map.get("nomJoueur"))) {
				b.putString("nom", str1[0]);
				b.putString("equipe", str1[1]);
				b.putString("point", str1[2]);
				b.putString("idJoueur", str1[3]);
			}
		}

		intent.putExtras(b);
		// start the second Activity
		this.startActivity(intent);
	}

	private HashMap<String, String> createJoueur(String key, String name) {
		HashMap<String, String> Pool = new HashMap<String, String>();
		Pool.put(key, name);
		return Pool;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if (m_ProgressDialog != null) {
				m_ProgressDialog.dismiss();
			}
			lesJoueurs = result;

			String str[] = result.split(";");
			int score = 0;
			for (int i = 0; i < str.length; i++) {
				String str1[] = str[i].split("/");
				score += Integer.parseInt(str1[2]);
				partList.add(createJoueur("nomJoueur", str1[0]));
			}
			textView.setText(textView.getText() + " " + Integer.toString(score));
			simpleAdpt.notifyDataSetChanged();
			textViewEmpty.setText("");

		}

		private HashMap<String, String> createJoueur(String key, String name) {
			HashMap<String, String> Pool = new HashMap<String, String>();
			Pool.put(key, name);
			return Pool;
		}

		@Override
		protected void onPreExecute() {
			if (m_ProgressDialog == null) {
				m_ProgressDialog = new ProgressDialog(ProfilpartActivity.this);
				m_ProgressDialog.setTitle(R.string.pd_title);
				m_ProgressDialog.setMessage(ProfilpartActivity.this
						.getString(R.string.pd_content));
				m_ProgressDialog.setCancelable(false);
				m_ProgressDialog.setIndeterminate(true);
			}
			listJoueurs = bd.getTousJoueurs(b.getInt("idPartSelect"),
					b.getInt("idPoolSelect"));

			for (int i = 0; i < listJoueurs.size(); i++) {
				listJoueur
						.add(listJoueurs.get(i).nomJoueur.replace(" ", "%20"));
			}

			m_ProgressDialog.show();
		}

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;

		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}

/*
 * package net.charlesdelmaire.qingpool;
 * 
 * import java.io.BufferedReader; import java.io.IOException; import
 * java.io.InputStream; import java.io.InputStreamReader; import
 * java.util.ArrayList; import java.util.HashMap; import java.util.Iterator;
 * import java.util.List; import java.util.Map;
 * 
 * import org.apache.http.HttpResponse; import
 * org.apache.http.client.HttpClient; import
 * org.apache.http.client.methods.HttpGet; import
 * org.apache.http.impl.client.DefaultHttpClient;
 * 
 * import android.app.Activity; import android.app.AlertDialog; import
 * android.app.ProgressDialog; import android.content.DialogInterface; import
 * android.content.Intent; import android.graphics.Bitmap; import
 * android.graphics.BitmapFactory; import android.net.ConnectivityManager;
 * import android.net.NetworkInfo; import android.os.AsyncTask; import
 * android.os.Bundle; import android.util.Log; import android.view.ContextMenu;
 * import android.view.ContextMenu.ContextMenuInfo; import android.view.Menu;
 * import android.view.MenuItem; import android.view.View; import
 * android.view.View.OnClickListener; import
 * android.widget.AdapterView.AdapterContextMenuInfo; import
 * android.widget.ImageView; import android.widget.ListView; import
 * android.widget.SimpleAdapter; import android.widget.TextView;
 * 
 * import com.google.analytics.tracking.android.EasyTracker; import
 * com.google.analytics.tracking.android.Fields; import
 * com.google.analytics.tracking.android.GoogleAnalytics; import
 * com.google.analytics.tracking.android.Tracker;
 * 
 * public class ProfilpartActivity extends Activity implements OnClickListener {
 * 
 * private ProgressDialog m_ProgressDialog; List<Map<String, String>> partList =
 * new ArrayList<Map<String, String>>(); SimpleAdapter simpleAdpt; String
 * lesJoueurs; // TextView agePart; TextView textView; TextView textView1;
 * TextView textViewEmpty; ImageView bmImage; Bundle b; private
 * QingPoolDatasource bd; private List<JoueurPool> listJoueurs; List<String>
 * listJoueur = new ArrayList<String>(); Participant participantSelect = null;
 * 
 * public void onCreate(Bundle savedInstanceState) {
 * 
 * super.onCreate(savedInstanceState); setContentView(R.layout.profilpart);
 * ListView lv = (ListView) findViewById(R.id.list); this.bd = new
 * QingPoolDatasource(this); this.bd.open(); textView = (TextView)
 * findViewById(R.id.scorePart); textView1 = (TextView)
 * findViewById(R.id.nomPart); textViewEmpty = (TextView)
 * findViewById(R.id.empty); View logoClick = findViewById(R.id.imageView1);
 * logoClick.setOnClickListener(this); b = getIntent().getExtras();
 * 
 * textView1.setText(textView1.getText() + " " + b.getString("nomPart"));
 * simpleAdpt = new SimpleAdapter(this, partList,
 * android.R.layout.simple_list_item_1, new String[] { "nomJoueur" }, new int[]
 * { android.R.id.text1 }); lv.setAdapter(simpleAdpt);
 * 
 * registerForContextMenu(lv); if (savedInstanceState != null) { lesJoueurs =
 * savedInstanceState.getString("listJoueur"); String str[] =
 * lesJoueurs.split(";"); int score = 0; for (int i = 0; i < str.length; i++) {
 * String str1[] = str[i].split("/"); score += Integer.parseInt(str1[2]);
 * partList.add(createJoueur("nomJoueur", str1[0])); }
 * textView.setText(textView.getText() + " " + Integer.toString(score));
 * simpleAdpt.notifyDataSetChanged(); } else {
 * 
 * participantSelect = bd.getParticipant(b.getInt("idPartSelect")); new
 * HttpAsyncTask()
 * .execute("http://charlesdelmaire1992.appspot.com/joueur?nom=");
 * 
 * } new DownloadImageTask((ImageView) findViewById(R.id.imagePart))
 * .execute(participantSelect.getImgPart().substring(0,
 * participantSelect.getImgPart().length() - 2) + "300");
 * 
 * 
 * Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
 * "UA-50075921-1");
 * 
 * HashMap<String, String> hitParameters = new HashMap<String, String>();
 * hitParameters.put(Fields.HIT_TYPE, "appview");
 * hitParameters.put(Fields.SCREEN_NAME,
 * getString(R.string.screen_profil_part));
 * 
 * tracker.send(hitParameters);
 * 
 * }
 * 
 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
 * menu; this adds items to the action bar if it is present.
 * getMenuInflater().inflate(R.menu.main, menu); return true; }
 * 
 * @Override public boolean onOptionsItemSelected(MenuItem item) { Intent intent
 * = null; switch (item.getItemId()) { case R.id.idRetour: intent = new
 * Intent(this, principaleActivity.class); intent.putExtras(b);
 * this.startActivity(intent); break; case R.id.deconnexion: intent = new
 * Intent(this, connexionActivity.class); b.putInt("deconnexion", 1);
 * intent.putExtras(b); this.startActivity(intent); break; case R.id.aide:
 * 
 * AlertDialog alertDialog = new AlertDialog.Builder(this).create();
 * alertDialog.setTitle(getString(R.string.menu_aide));
 * alertDialog.setMessage(getString(R.string.aide_prof_part));
 * alertDialog.setButton(getString(R.string.fermer), new
 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
 * dialog, int which) { dialog.dismiss(); } }); // Set the Icon for the Dialog
 * alertDialog.setIcon(R.drawable.aide); alertDialog.show();
 * 
 * break; }
 * 
 * return super.onOptionsItemSelected(item); }
 * 
 * @Override public void onSaveInstanceState(Bundle outState) {
 * super.onSaveInstanceState(outState); outState.putString("listJoueur",
 * lesJoueurs); }
 * 
 * @Override public void onStart() { super.onStart(); bd.open();
 * EasyTracker.getInstance(this).activityStart(this); }
 * 
 * @Override public void onResume() { super.onResume(); }
 * 
 * @Override public void onStop() { super.onStop(); bd.close();
 * EasyTracker.getInstance(this).activityStop(this); }
 * 
 * @Override public void onClick(View arg0) { if (arg0.getId() ==
 * R.id.imageView1) { Intent intent = null; intent = new Intent(this,
 * principaleActivity.class); intent.putExtras(b); this.startActivity(intent); }
 * }
 * 
 * public String GET(String url) { InputStream inputStream = null; String result
 * = ""; String result2 = ""; try {
 * 
 * ArrayList<String> liste = null;
 * 
 * for (Iterator<String> i = listJoueur.iterator(); i.hasNext();) { String item
 * = i.next();
 * 
 * // create HttpClient HttpClient httpclient = new DefaultHttpClient();
 * 
 * // make GET request to the given URL HttpResponse httpResponse =
 * httpclient.execute(new HttpGet(url .concat(item)));
 * 
 * // receive response as inputStream inputStream =
 * httpResponse.getEntity().getContent();
 * 
 * // convert inputstream to string if (inputStream != null) result2 =
 * convertInputStreamToString(inputStream); else result2 =
 * getString(R.string.erreur);
 * 
 * // result += result2; liste = JsonParser.unePersonne(result2); result +=
 * liste.get(0) + ";"; }
 * 
 * } catch (Exception e) { Log.d("InputStream", e.getLocalizedMessage()); }
 * 
 * return result; }
 * 
 * private static String convertInputStreamToString(InputStream inputStream)
 * throws IOException { BufferedReader bufferedReader = new BufferedReader( new
 * InputStreamReader(inputStream)); String line = ""; String result = ""; while
 * ((line = bufferedReader.readLine()) != null) result += line;
 * 
 * inputStream.close(); return result;
 * 
 * }
 * 
 * public boolean isConnected() { ConnectivityManager connMgr =
 * (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
 * NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); if (networkInfo !=
 * null && networkInfo.isConnected()) return true; else return false; }
 * 
 * public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo
 * menuInfo) { super.onCreateContextMenu(menu, v, menuInfo);
 * AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;
 * 
 * // We know that each row in the adapter is a Map HashMap map = (HashMap)
 * simpleAdpt.getItem(aInfo.position); Bundle b = new Bundle(); Intent intent =
 * new Intent(this, ProfilJoueActivity.class); String str[] =
 * lesJoueurs.split(";");
 * 
 * for (int i = 0; i < str.length; i++) { String str1[] = str[i].split("/");
 * 
 * if (str1[0].equals(map.get("nomJoueur"))) { b.putString("nom", str1[0]);
 * b.putString("equipe", str1[1]); b.putString("point", str1[2]);
 * b.putString("idJoueur", str1[3]); } }
 * 
 * intent.putExtras(b); // start the second Activity this.startActivity(intent);
 * }
 * 
 * private HashMap<String, String> createJoueur(String key, String name) {
 * HashMap<String, String> Pool = new HashMap<String, String>(); Pool.put(key,
 * name); return Pool; }
 * 
 * private class HttpAsyncTask extends AsyncTask<String, Void, String> {
 * 
 * @Override protected String doInBackground(String... urls) { return
 * GET(urls[0]); }
 * 
 * // onPostExecute displays the results of the AsyncTask.
 * 
 * @Override protected void onPostExecute(String result) { if (m_ProgressDialog
 * != null) { m_ProgressDialog.dismiss(); } lesJoueurs = result;
 * 
 * String str[] = result.split(";"); int score = 0; for (int i = 0; i <
 * str.length; i++) { String str1[] = str[i].split("/"); score +=
 * Integer.parseInt(str1[2]); partList.add(createJoueur("nomJoueur", str1[0]));
 * } textView.setText(textView.getText() + " " + Integer.toString(score));
 * simpleAdpt.notifyDataSetChanged(); textViewEmpty.setText("");
 * 
 * }
 * 
 * private HashMap<String, String> createJoueur(String key, String name) {
 * HashMap<String, String> Pool = new HashMap<String, String>(); Pool.put(key,
 * name); return Pool; }
 * 
 * @Override protected void onPreExecute() { if (m_ProgressDialog == null) {
 * m_ProgressDialog = new ProgressDialog(ProfilpartActivity.this);
 * m_ProgressDialog.setTitle(R.string.pd_title);
 * m_ProgressDialog.setMessage(ProfilpartActivity.this
 * .getString(R.string.pd_content)); m_ProgressDialog.setCancelable(false);
 * m_ProgressDialog.setIndeterminate(true); } listJoueurs =
 * bd.getTousJoueurs(b.getInt("idPartSelect"), b.getInt("idPoolSelect"));
 * 
 * for (int i = 0; i < listJoueurs.size(); i++) { listJoueur
 * .add(listJoueurs.get(i).nomJoueur.replace(" ", "%20")); }
 * 
 * m_ProgressDialog.show(); }
 * 
 * }
 * 
 * private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
 * ImageView bmImage;
 * 
 * public DownloadImageTask(ImageView bmImage) { this.bmImage = bmImage;
 * 
 * }
 * 
 * protected Bitmap doInBackground(String... urls) { String urldisplay =
 * urls[0]; Bitmap mIcon11 = null; try { InputStream in = new
 * java.net.URL(urldisplay).openStream(); mIcon11 =
 * BitmapFactory.decodeStream(in); } catch (Exception e) { Log.e("Error",
 * e.getMessage()); e.printStackTrace(); } return mIcon11; }
 * 
 * protected void onPostExecute(Bitmap result) { bmImage.setImageBitmap(result);
 * } } }
 */