package com.example.jahan.uidemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jahan.uidemo.GPSTracker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.concurrent.ExecutionException;


public class sharPCmap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowLongClickListener {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleMap mMap;
    //ketabkhuneye gps baraye yaftane current location
    GPSTracker gpsTracker;
    Location currentLocation;
    double currentLat;
    double currentLon;
    LatLng position;
    MarkerOptions markerOptions;
    ArrayList<LatLng> MarkerPoints;
    notification notification;

    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    SupportMapFragment mapFragment;
    private OnInfoWindowElemTouchListener infoButtonListener;

   final private int PERMISSION_REQUEST_CODE_LOCATION=321;

    notification notif;

    MapWrapperLayout mapWrapperLayout;
    String free_space;
    String total_space;
    int update_space;
    String m1;
    int space;
    int total;
    double marker_lt;
    double marker_ln;
    Marker m;
    HttpPostRequest ht;




    //---------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shar_pcmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);



        gpsTracker = GPSTracker.getInstance(this);
        //baraye payame"roshn krdane location"

        if (!gpsTracker.canGetLocation()) {
            Log.e("hi guys", "hi");
            gpsTracker.showSettingsAlert();
        }
        Log.e("ucheetori","kho");
        if (Build.VERSION.SDK_INT >= 23 || true){


            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,getApplicationContext(),this)) {
                //You fetch the Location here
                currentLocation = gpsTracker.getLocation();
                mapFragment.getMapAsync(this);
                //code to use the
            }
            else
            {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSION_REQUEST_CODE_LOCATION,getApplicationContext(),this);
            }

        }
        else
            currentLocation = gpsTracker.getLocation();
        Log.e("mahyar",""+currentLocation);






        };




//    public void onMapSearch(View view) {
////        EditText locationSearch = (EditText) findViewById(R.id.editText);
////        String location = locationSearch.getText().toString();
//        List<Address> addressList = null;
//        Log.e("address", location);
//        if (location != null || !location.equals("")) {
//            Geocoder geocoder = new Geocoder(this);
//            Log.e("address2", location);
//            try {
//                addressList = geocoder.getFromLocationName(location, 1);
//
//                Log.e("address3", location);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Address address = addressList.get(0);
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//            Log.e("address4", location);
//        }
//    }


    //--------------------------------------------------------------------------------------
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }




    //----------------------------------------------------------------------------------------


//----------------------------------------------------------------------------------


    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    //--------------------------------------------------------------------------------------------------------
// Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

//---------------------------------------------------------------------------------------------------------

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
//--------------------------------------------------------------------------------------------------

    public void requestPermission(String strPermission, int perCode, Context _c, Activity _a){

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission)){
            Toast.makeText(this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);
        }
    }
//-----------------------------------------------------------------------------------------------------
    public static boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("tajrobe","");
                    currentLocation = gpsTracker.getLocation();
                    mapFragment.getMapAsync(this);


                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
                break;

        }
    }
    public void onMapReady(GoogleMap map) {
        mMap = map;
        // Retrieve location and camera position from saved instance state.
        Log.e("ucheetori","khobammmm");
        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("ucheetori","khob");
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(sharPCmap.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }



        });

        //daryafte current location ba ketabkhuneye GPStracker
        if (currentLocation != null) {

            currentLat = currentLocation.getLatitude();
            currentLon = currentLocation.getLongitude();
            Log.e("cee", String.valueOf(currentLat) + String.valueOf(currentLon));
            position = new LatLng(currentLat, currentLon);
            //zoom roo nghshe
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLon), 17.0f));

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    // Already two locations
                    if (MarkerPoints.size() > 1) {
                        MarkerPoints.clear();
                        mMap.clear();
                    }

                    // Adding new item to the ArrayList
                    MarkerPoints.add(point);

                    // Creating MarkerOptions
                    MarkerOptions options = new MarkerOptions();

                    // Setting the position of the marker
                    options.position(point);

                    /**
                     * For the start location, the color of marker is GREEN and
                     * for the end location, the color of marker is RED.
                     */
                    if (MarkerPoints.size() == 1) {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    } else if (MarkerPoints.size() == 2) {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }


                    // Add new marker to the Google Map Android API V2
                    mMap.addMarker(options);

                    // Checks, whether start and end locations are captured
                    if (MarkerPoints.size() >= 2) {
                        LatLng origin = MarkerPoints.get(0);
                        LatLng dest = MarkerPoints.get(1);

                        // Getting URL to the Google Directions API
                        String url = getUrl(origin, dest);
                        Log.d("onMapClick", url.toString());
                        FetchUrl FetchUrl = new FetchUrl();

                        // Start downloading json data from Google Directions API
                        FetchUrl.execute(url);
                        //move map camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(9));
                    }

                }
            });

//            mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));
//
//            // We want to reuse the info window for all the markers,
//            // so let's create only one class member instance
//            this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window, null);
//            this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
//            this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
//            this.infoButton = (Button)infoWindow.findViewById(R.id.button);
//
//            // Setting custom OnTouchListener which deals with the pressed state
//            // so it shows up
//            this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
//                    getResources().getDrawable(R.drawable.common_google_signin_btn_text_light), //btn_default_normal_holo_light
//                    getResources().getDrawable(R.drawable.common_google_signin_btn_text_dark)) //btn_default_pressed_holo_light
//            {
//                @Override
//                protected void onClickConfirmed(View v, Marker marker) {
//                    // Here we can perform some action triggered after clicking the button
//                    Log.e("letsstart","ok");
//                    String m1  = marker.getTitle();
//                    String URL = "http://thingtalk.ir/channels/"+m1.split(" ")[0]+"/feed.json?key="+m1.split(" ")[1]+"&results=1";
//                    //String URL = "http://thingtalk.ir/channels/526/feed.json?key=640CPW00RFZZNVYI&result=1";
//                    sharPCmap.HttpGetRequest ht = new sharPCmap.HttpGetRequest();
//                    ;
//                    try {
//                        String result = ht.execute(URL).get();
//                        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//                        Log.e("bye","babye...");
//
//                        JSONObject jsonobj = new JSONObject(result);
//                        JSONArray array = jsonobj.getJSONArray("feeds");
//                        Log.e("again",":(");
//
//
//                        for (int i = 0; i < array.length(); i++) {
//                            Log.e("hi guys","hi...");
//                            JSONObject data = array.getJSONObject(i);
//
//                            free_space = data.getString("field2");
//
//                        }
//
//                        Toast.makeText(getApplicationContext(),"free space : "+ free_space,Toast.LENGTH_SHORT).show();
//
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//
//                }
//            };
//            this.infoButton.setOnTouchListener(infoButtonListener);
//
//
//            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                @Override
//                public View getInfoWindow(Marker marker) {
//                    return null;
//                }
//
//                @Override
//                public View getInfoContents(Marker marker) {
//                    // Setting up the infoWindow with current's marker info
//                    infoTitle.setText(marker.getTitle());
//                    infoSnippet.setText(marker.getSnippet());
//                    infoButtonListener.setMarker(marker);
//
//                    // We must call this to set the current marker and infoWindow references
//                    // to the MapWrapperLayout
//                    mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
//                    return infoWindow;
//                }
//            });


            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    //.title("مکان فعلی")
                    .snippet("مکان فعلی")
                    .draggable(true)
                    //zoom the camera in map
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            m.showInfoWindow();

            m.setTitle("502 PBVR6GZN6FF1SJZD");


//add marker to the parking location
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(35.761891, 51.420329))
                    .snippet("پارکینگ باشگاه ورزشی میرداماد")
                    .title("525 WHABZT9NH6M83X2J")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            ).showInfoWindow();




//add marker to the current location


            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(35.760955,51.404437))
                    .title("524 PGOQALFBOEWYVSX9")
                    .snippet("پارکینگ خیابان ونک")

                    .draggable(true)
                    //zoom the camera in map
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            ).showInfoWindow();



            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(35.756925,51.409313))
                    .title("526 640CPW00RFZZNVYI")
                    .snippet("پارکینگ میدان ونک")
                    .draggable(true)
                    //zoom the camera in map
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            ).showInfoWindow();



            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Log.e("t","y");
                    m1 = marker.getTitle();
                    marker_lt = marker.getPosition().latitude;
                    marker_ln = marker.getPosition().longitude;
                    String URL = "http://thingtalk.ir/channels/" + m1.split(" ")[0] + "/feed.json?key=" + m1.split(" ")[1] + "&results=1";
                    HttpGetRequest ht = new HttpGetRequest();
                    try {
                        String result = ht.execute(URL).get();
//
                        Log.e("bye", "babye...");

                        JSONObject jsonobj = new JSONObject(result);
                        JSONArray array = jsonobj.getJSONArray("feeds");
                        Log.e("again", ":(");


                        for (int i = 0; i < array.length(); i++) {
                            Log.e("hi guys", "hi...");
                            JSONObject data = array.getJSONObject(i);
                            total_space = data.getString("field1");
                            free_space = data.getString("field2");
                        }
                        space = Integer.parseInt(free_space);
                        total = Integer.parseInt(total_space);
                        if (space > 0) {
                            update_space = space - 1;
                            if (update_space >= 0) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(sharPCmap.this);
                                alertDialogBuilder.setPositiveButton("رزرو", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String URL = "http://thingtalk.ir/update?key=" + m1.split(" ")[1] + "&field1=" + total + "&field2=" + update_space + "&field3=" + marker_lt + "&field4=" + marker_ln;

                                        HttpPostRequest ht = new HttpPostRequest();

                                        // current activity
                                        try {
                                            String result = ht.execute(URL).get();

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }


                                        // sharPCmap.this.finish();
                                    }
                                }) .setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                }).show();
                            }
                        }
                        if (space == 0) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(sharPCmap.this);
                            alertDialogBuilder.setPositiveButton("لیست انتظار", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                String URL = "http://thingtalk.ir/update?key=" + m1.split(" ")[1] + "&field1=" + total + "&field2=" + update_space + "&field3=" + marker_lt + "&field4=" + marker_ln;
//                                HttpPostRequest ht = new HttpPostRequest();
                                    // current activity
//                                try {
//                                    String result = ht.execute(URL).get();
//
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                } catch (ExecutionException e) {
//                                    e.printStackTrace();
//                                }
                                    //  sharPCmap.this.finish();
                                }
                            }) .setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            }).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                    return false;
                }
            });



        }





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("parisa",resultCode + " " + requestCode+ " "+RESULT_OK );
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e("parpar",data.toString() );
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latlng = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 20.0f));
                Log.i("par", "Place: ");
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("isa", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    private class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;

            String inputLine;

            try {
                URL myUrl = new URL(stringUrl);


                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);


                connection.connect();

                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();


                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();


                result = stringBuilder.toString();
                System.out.print(result);
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }

            return result;


        }


        protected void onPostExecute(String result) {


            super.onPostExecute(result);


        }


    }
    //--------------------------------------------------------------------------------------------------
    public class DataParser {

        /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
        public List<List<HashMap<String,String>>> parse(JSONObject jObject){

            List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for(int i=0;i<jRoutes.length();i++){
                    jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<>();

                    /** Traversing all legs */
                    for(int j=0;j<jLegs.length();j++){
                        jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for(int k=0;k<jSteps.length();k++){
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for(int l=0;l<list.size();l++){
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString((list.get(l)).latitude) );
                                hm.put("lng", Double.toString((list.get(l)).longitude) );
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
            }


            return routes;
        }


        /**
         * Method to decode polyline points
         * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         * */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }
    //--------------------------------------------------------------------------------------------------
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }


        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(20);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }

    }

//-----------------------------------------------------------------------------------------------------------

//search

    //-------------------------------------------------------------------------------------

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "clicked",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Toast.makeText(this, "reserved",
                Toast.LENGTH_SHORT).show();

    }


    //................................................................
//!




}
