package net.charlesdelmaire.qingpool;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {
	private static final String MSG_KEY = "key";
	private static final String MSG_RESULT = "result";
	private static final String MSG_SUCCESS = "success";
	private static final String MSG_ERROR = "error";

	public static ArrayList<String> parseListePersonne(String p_body)
			throws JSONException {
		ArrayList<String> liste = null;
		JSONObject jObj = new JSONObject(p_body);

		if (jObj.getString(MSG_RESULT).equals(MSG_SUCCESS)) {
			liste = new ArrayList<String>();
			JSONArray tab = jObj.getJSONArray("lesJoueurs");
			for (int i = 0; i < tab.length(); i++) {
				JSONObject objPers = tab.getJSONObject(i);
				liste.add(objPers.getString("nom") + " "
						+ objPers.getString("team"));
			}
		} else {
			Log.w("parseListePersonne", "No success from web service : "
					+ p_body);
		}

		return liste;
	}

	public static ArrayList<String> unePersonne(String p_body)
			throws JSONException {
		ArrayList<String> liste = null;
		JSONObject jObj = new JSONObject(p_body);

		if (jObj.getString(MSG_RESULT).equals(MSG_SUCCESS)) {
			liste = new ArrayList<String>();
			JSONArray tab = jObj.getJSONArray("lesJoueurs");
			for (int i = 0; i < tab.length(); i++) {
				JSONObject objPers = tab.getJSONObject(i);
				liste.add(objPers.getString("nom") + "/"
						+ objPers.getString("team") + "/"
						+ objPers.getString("p"));
			}
		} else {
			Log.w("parseListePersonne", "No success from web service : "
					+ p_body);
		}

		return liste;
	}

}
