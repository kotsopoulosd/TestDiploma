package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 12/10/2015.
 */

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventfullActivity extends ListActivity {

    static final String TAG="Eventfull";
    static final String SERVICE = "http://api.eventful.com/json/events/search?app_key=jzw49WKzvQhTHdfn&";
    static final double Radius = 6.2;
    static final String Month=new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime());
    /*Here we can provide Shared Preferences for specific location or find the city coordinates*/
    private final LatLng Thessaloniki=new LatLng(40.626138,22.948773);
    private LatLng x=new LatLng(0,0);
    protected String mServiceUrl;
    HttpURLConnection conn;
    EventfullAdapter adapter;
    ListView list;
    URL url = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mServiceUrl = SERVICE;
        String urlstring =getUrl(Thessaloniki);
        try {
            url = new URL(urlstring);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Connection().execute();

        super.onCreate(savedInstanceState);
    }


    public String getUrl(LatLng x)
    {
        StringBuffer urlString = new StringBuffer(mServiceUrl);
        Log.d("Longitude " + Double.toString(x.longitude), "Latitude " + Double.toString(x.latitude));
        urlString.append("where=" +Double.toString(x.latitude)+ "," +Double.toString(x.longitude));
        urlString.append("&within=" + Double.toString(Radius));
        urlString.append("&when=" +Month);
        Log.d(TAG, urlString.toString());
        return urlString.toString();

    }

    private class Connection extends AsyncTask {
        ProgressDialog progressDialog;
        public List<EventFullObject> RequestEvents = new ArrayList<EventFullObject>();
        @Override
        protected void onPostExecute(Object o)
        {
            progressDialog.dismiss();
            super.onPostExecute(o);

            adapter=new EventfullAdapter(getApplicationContext(),R.layout.row_eventful,RequestEvents);
            setListAdapter(adapter);
        }
        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(EventfullActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            progressDialog.setTitle(getResources().getString(R.string.wait))  ;
            progressDialog.setMessage(getResources().getString(R.string.Eventmessage));
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... arg0) {

            try {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;

                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                Log.d(TAG, responseStrBuilder.toString());
                JSONObject jObject=new JSONObject(responseStrBuilder.toString());

                JSONObject jInstructions = jObject.getJSONObject("events");
                JSONArray jsonArray=jInstructions.getJSONArray("event");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    EventFullObject event = new EventFullObject();
                    JSONObject jSummary = jsonArray.getJSONObject(i);
                    event.setEventTitle(optString(jSummary, "title"));
                    event.setdescription(optString(jSummary, "description"));
                    event.setEventURL(optString(jSummary, "url"));
                    event.setStart_time(optString(jSummary, "start_time"));
                    event.setStop_time(optString(jSummary, "stop_time"));
                    event.setVenue_name(optString(jSummary, "venue_name"));
                    event.setVenueURL(optString(jSummary, "venue_url"));
                    event.setvenue_address(optString(jSummary, "venue_address"));
                    event.setCity(optString(jSummary, "city_name"));
                    RequestEvents.add(event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        }

    public static String optString(JSONObject json, String key)
    {
        if (json.isNull(key))
            return "-";
        else
            return json.optString(key, null);
    }
}

