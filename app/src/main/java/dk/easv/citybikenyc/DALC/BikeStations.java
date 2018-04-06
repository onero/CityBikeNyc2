package dk.easv.citybikenyc.DALC;

import android.util.Log;

import dk.easv.citybikenyc.Global;
import dk.easv.citybikenyc.IBELoaded;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class BikeStations extends ABikeStations {
	
	private final String URL = "http://feeds.citibikenyc.com/stations/stations.json";

	ArrayList<BEBikeStation> m_districts;

    private static AsyncHttpClient client;

    IBELoaded m_beLoaded;
	
	public BikeStations(IBELoaded beLoadedHandler){
        m_beLoaded = beLoadedHandler;
		m_districts = new ArrayList<>();
        client = new AsyncHttpClient();
    }

    /**
     * Simple mockup function - can be used when testing offline.
     */
    public void loadFake()
    {
        m_districts.add(new BEBikeStation(1, "a"));
        m_districts.add(new BEBikeStation(2, "b"));
        m_districts.add(new BEBikeStation(3, "c"));
    }

	@Override
    public void loadAll()
	{
	    //client.setTimeout(90000);

        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d(Global.TAG, "onSuccess ");
                jsonStringToBusinessEntities(new String(response));
                m_beLoaded.onFinished();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(Global.TAG, "onFailure " + statusCode);
                if (errorResponse != null)
                   Log.d(Global.TAG, "error response = " + errorResponse);
                else
                    Log.d(Global.TAG, "Error response = null");

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
	}

    private void jsonStringToBusinessEntities(String result)
    {
        if (result.startsWith("error"))
        {
            Log.d(Global.TAG, "Error: " + result);
            return;
        }

        if (result == null)
        {
            Log.d(Global.TAG, "Error: NO RESULT");
            return;
        }
        JSONObject all = null;
        JSONArray array = null;
        try {
            all = new JSONObject(result);

            array = all.getJSONArray("stationBeanList");


        for (int i = 0; i < array.length(); i++) {
            m_districts.add(new BEBikeStation(array.getJSONObject(i)));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	@Override
    public ArrayList<BEBikeStation> getAll()
	{
        return m_districts;
    }




}
