package dk.easv.citybikenyc.DALC;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dk.easv.citybikenyc.Global;
import dk.easv.citybikenyc.IBELoaded;


/**
 * Created by oe on 05/04/2018.
 */

public class BikeStationsASyncTask extends ABikeStations {


    public static final String JSON_ARRAY_NAME = "stationBeanList";
    private static final String URL = "http://feeds.citibikenyc.com/stations/stations.json";

    ArrayList<BEBikeStation> m_districts;


    InitializeTask mTask;

    public BikeStationsASyncTask(IBELoaded beLoadedHandler){
        m_districts = new ArrayList<>();
        mTask = new InitializeTask(this, beLoadedHandler);
    }

    public void loadAll()
    {
        mTask.execute();
    }

    public void loadAllBlocking()
    {
        try {
            String result = queryRESTurl(URL);
            if (result == null) return;
            jsonStringToBusinessEntities(result);
            } catch (Exception e)
        {  Log.d(Global.TAG, "General exception in loadAll " + e.getMessage());
        }
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
        JSONObject all;
        JSONArray array;
        try {
            all = new JSONObject(result);

            array = all.getJSONArray(JSON_ARRAY_NAME);


            for (int i = 0; i < array.length(); i++) {
                m_districts.add(new BEBikeStation(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<BEBikeStation> getAll()
    { return m_districts; }


    private String queryRESTurl(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        Log.d(Global.TAG,"DefaultHttpClient created");
        HttpGet httpget = new HttpGet(url);
        Log.d(Global.TAG, "HttpGet from " + url + " created");
        HttpResponse response;

        try {
            response = httpclient.execute(httpget);
            Log.d(Global.TAG, "HttpResponse executed");
            HttpEntity entity = response.getEntity();
            Log.d(Global.TAG, "Entity loaded");

            if (entity != null) {

                InputStream in = entity.getContent();
                String result = convertStreamToString(in);
                in.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            Log.d(Global.TAG,
                    "There was a protocol based error", e);
        } catch (IOException e) {
            Log.d(Global.TAG,
                    "There was an IO Stream related error", e);
        } catch (Exception e)
        {
            Log.d(Global.TAG,"generel exception " + e.getMessage());
        }

        return null;
    }

    private String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }



    @SuppressLint("StaticFieldLeak")


    public class InitializeTask extends AsyncTask<
            Void, // input to doInBackground
            Void, // progress
            ArrayList<BEBikeStation>> // output of doInBackground
    {

        BikeStationsASyncTask mBikeStations;

        IBELoaded mCallback;

        public InitializeTask(BikeStationsASyncTask bikeStations, IBELoaded callBack)
        {
            this.mBikeStations = bikeStations;
            mCallback = callBack;
        }

        @Override
        protected ArrayList<BEBikeStation> doInBackground(Void... v) {

            Log.d(Global.TAG, "in doInBackGround ");
            mBikeStations.loadAllBlocking();
            return mBikeStations.getAll();

        }
        // onPostExecute displays the results of the AsyncTask.doInBackground().
        // this method is invoked by the GUI thread
        @Override
        protected void onPostExecute(final ArrayList<BEBikeStation> s) {
             mCallback.onFinished();
        }

    }

}
