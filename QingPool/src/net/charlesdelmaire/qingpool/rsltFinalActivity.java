package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class rsltFinalActivity extends Activity {
	private Bundle b;
	private List<Participant> lstPart;
	List<Map<String, String>> partList = new ArrayList<Map<String, String>>();
	private QingPoolDatasource bd;
	
	SimpleAdapter simpleAdpt;
	private List<String> liste = new ArrayList<String>();	
	ListView lv = (ListView) findViewById(R.id.list);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rsltfinal);
		b = getIntent().getExtras();
		getActionBar().setHomeButtonEnabled(true);
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");
		this.bd = new QingPoolDatasource(this);
		this.bd.open();
		lstPart = bd.getTousPart(b.getInt("idPoolSelect"));	
		
		initList();
		simpleAdpt = new SimpleAdapter(this, partList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomPart" }, new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_rslt_pool));
		tracker.send(hitParameters);
	}	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
			case android.R.id.home:            
		        intent = new Intent(this, principaleActivity.class);   
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		        startActivity(intent); 
		        break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initList() {

		for (int i = 0; i < lstPart.size(); i++) {			
			liste.add(lstPart.get(i).getNomPart());
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
