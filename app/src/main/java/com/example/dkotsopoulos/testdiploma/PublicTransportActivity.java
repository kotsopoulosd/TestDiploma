package com.example.dkotsopoulos.testdiploma;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublicTransportActivity extends FragmentActivity {

    public static LatLng Thessaloniki = new LatLng(40.626340, 22.948351);
    public static LatLng origin;
    public static LatLng dest;
    public String departure_time;
    public String tmode;
    public int locationID;
    public String sensor;
    public static String TransportMode; //driving  for car , transit  for public transport, walking for walk
    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    List<String> Directions= new ArrayList<String>();
    Polyline CurrentLine;
    Integer passPostexecute=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            Bundle extras = getIntent().getExtras();
            Directions=null;
            // Sensor enabled
            sensor = "sensor=false";
            //Transport Mode
            String[] ModeandLocation= extras.getString("TransportMode").split(",");
            TransportMode = ModeandLocation[0];
            locationID= Integer.parseInt(ModeandLocation[1]);

            setContentView(R.layout.activity_public_transport);
            map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
            map.setMyLocationEnabled(true);
            if (TransportMode.equals("transit"))
            {
                Toast.makeText(getApplicationContext(), "Currently, Transit not available in the city",Toast.LENGTH_SHORT).show();
                TransportMode="driving";
            }
            tmode = "mode="+TransportMode;
            departure_time = "departure_time="+String.valueOf(System.currentTimeMillis());
            map.clear();
            SharedPreferences coord = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (Double.valueOf(coord.getString("LatHome", ""))!=0) {
                passPostexecute=1;
                if (locationID == 0) {//Home to Work
                    origin = new LatLng(Double.valueOf(coord.getString("LatHome", "")), Double.valueOf(coord.getString("LogHome", "")));
                    dest = new LatLng(Double.valueOf(coord.getString("LatWork", "")), Double.valueOf(coord.getString("LogWork", "")));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(Thessaloniki, 12));
                    map.addMarker(new MarkerOptions().position(origin).title("Origin").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    map.addMarker(new MarkerOptions().position(dest).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    String url = getDirectionsUrl(origin, dest);
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);
                } else if (locationID == 1) {//Work to Home
                    origin = new LatLng(Double.valueOf(coord.getString("LatWork", "")), Double.valueOf(coord.getString("LogWork", "")));
                    dest = new LatLng(Double.valueOf(coord.getString("LatHome", "")), Double.valueOf(coord.getString("LogHome", "")));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(Thessaloniki, 12));
                    map.addMarker(new MarkerOptions().position(origin).title("Origin").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    map.addMarker(new MarkerOptions().position(dest).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    String url = getDirectionsUrl(origin, dest);
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);
                } else {

                    if (CurrentLine != null) CurrentLine.remove();


                    // Initializing array List
                    markerPoints = new ArrayList<LatLng>();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(Thessaloniki, 12));
                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng point) {
                            markerPoints.add(point);
                            // Creating MarkerOptions
                            MarkerOptions options = new MarkerOptions();
                            // Setting the position of the marker
                            options.position(point);
                            if (markerPoints.size() == 1) {
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            } else if (markerPoints.size() == 2) {
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            }
                            map.addMarker(options);
                            if (markerPoints.size() >= 2) {
                                origin = markerPoints.get(0);
                                dest = markerPoints.get(1);
                                String url = getDirectionsUrl(origin, dest);
                                DownloadTask downloadTask = new DownloadTask();
                                downloadTask.execute(url);
                            }
                        }
                    });
                }
            }
            else
            {
                passPostexecute=0;
                Toast.makeText(getApplicationContext(), "Not Yet Clustering info", Toast.LENGTH_SHORT).show();
            }
            super.onCreate(savedInstanceState);
            }

    @Override
    protected void onPause() {

        if(CurrentLine!=null)CurrentLine.remove();
        map.clear();
        super.onPause();
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

            // Origin of route
            String str_origin = "origin="+origin.latitude+","+origin.longitude;
            // Destination of route
            String str_dest = "destination="+dest.latitude+","+dest.longitude;
            // Building the parameters to the web service
            String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+tmode+"&"+departure_time;

            // Output format
            String output = "json";
            String language = "language=en";
            String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&"+language;

            return url;
            }

    private String downloadUrl(String strUrl) throws IOException{
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
            sb.append(line);
            }

            data = sb.toString();

            br.close();

            }catch(Exception e){
            Log.d("Exception while", e.toString());
            }finally{
            iStream.close();
            urlConnection.disconnect();
            }
            return data;
            }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (passPostexecute == 1) {
                ParserTask parserTask = new ParserTask();

                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);
                Button InstructionButton = (Button) findViewById(R.id.InstructionButton);
                InstructionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(PublicTransportActivity.this, InstructionActivity.class).putStringArrayListExtra("Instructions", (ArrayList<String>) Directions));
                        Log.d("Instruction", Directions.toString());
                    }
                });

            }
            else
                Toast.makeText(getApplicationContext(), "Not Yet Clustering info", Toast.LENGTH_SHORT).show();
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;


            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
               Directions=parser.Directions;
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;

            MarkerOptions markerOptions = new MarkerOptions();

            PolylineOptions lineOptions= new PolylineOptions();
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(7);
                lineOptions.color(Color.GREEN);
            }

            // Drawing polyline in the Google Map for the i-th route
            CurrentLine = map.addPolyline(lineOptions);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_public_transport, menu);
        return true;
    }
}