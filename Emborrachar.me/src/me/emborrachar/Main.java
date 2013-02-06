package me.emborrachar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import cl.skyvortex.connection.HttpConnection;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("HandlerLeak")
public class Main extends android.support.v4.app.FragmentActivity implements LocationListener {
	private EditText searchBar;
	private GoogleMap mapa;
	private String bestProvider;
	private final int INICIAL=1;
	private final int BUSQUEDA=0;
	private LocationManager locationManager;
	private BotilleriaList mList;
	private final double DEFAULT_LAT = -33.44462;
	private final double DEFAULT_LONG = -70.655222;
	
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==INICIAL)
				displayInitialData(msg.getData().getString("api_response"));
			else
				displaySearchData(msg.getData().getString("api_response"));
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = new Intent(this, Splash.class);
		startActivity(i);
		setContentView(R.layout.main);
		String city="Santiago";
		locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		try{
			Criteria criteria = new Criteria();
			bestProvider = locationManager.getBestProvider(criteria, true);
			if(bestProvider==null)
				bestProvider = locationManager.getBestProvider(criteria, false);
			Location loc = locationManager.getLastKnownLocation(bestProvider);
			Geocoder geoCoder = new Geocoder(this, new Locale("es-CL"));
			List<Address> addresses = geoCoder.getFromLocation(loc.getLatitude(),  loc.getLongitude(), 1);
			city = addresses.get(0).getLocality();
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch(Exception e){
		}
		getBotillerias(city, true);
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.getUiSettings().setZoomControlsEnabled(false);
		searchBar = (EditText) findViewById(R.id.search_bar);
	}

	private void displayInitialData(String apiResponse){
		mList = new BotilleriaList();
		mList.parseList(apiResponse);
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mapa.setInfoWindowAdapter(new BorrachoAdapter(this));
		try{
			Criteria criteria = new Criteria();
			bestProvider = locationManager.getBestProvider(criteria, true);
			if(bestProvider==null)
				bestProvider = locationManager.getBestProvider(criteria, false);
			Location loc = locationManager.getLastKnownLocation(bestProvider);
			mapa.addMarker(new MarkerOptions()
			.position(new LatLng(loc.getLatitude(),loc.getLongitude()))
			.title("Tú estás aquí")
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(),loc.getLongitude()), (float)15.0);
			mapa.moveCamera(cam);
		}catch(NullPointerException e){
			mapa.addMarker(new MarkerOptions()
			.position(new LatLng(DEFAULT_LAT,DEFAULT_LONG))
			.title("Santiago de Chile")
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(DEFAULT_LAT,DEFAULT_LONG), (float)15.0);
			mapa.moveCamera(cam);
		}catch(Exception e){
			
		}
		showStores();
	}

	protected void onResume(){
		super.onResume();
	}
	
	private void displaySearchData(String apiResponse){
		mList = new BotilleriaList();
		mList.parseList(apiResponse);
		if(mList.count()!=0){
			try{
				CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(mList.get(0).getLatitud(),mList.get(0).getLongitud()), (float)15.0);
				mapa.moveCamera(cam);
			}catch(NullPointerException e){
				CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(DEFAULT_LAT,DEFAULT_LONG), (float)15.0);
				mapa.moveCamera(cam);
			}
		}
		showStores();
	}
	
	private void showStores(){
		for(int i=0; i<mList.count(); i++){
			Botilleria b = mList.get(i);
			try{
				mapa.addMarker(new MarkerOptions()
				.position(new LatLng(b.getLatitud(),b.getLongitud()))
				.title(b.getName())
				.snippet(b.getSnippet())
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			}catch(NullPointerException e){
				continue;
			}
		}
	}

	private void getBotillerias(final String city, final boolean inicial){
		new Thread(new Runnable(){
			public void run(){
				HttpConnection conn = new HttpConnection();
				ArrayList<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
				Log.d("ld", city);
				params.add(Pair.create("nombre", remove(city)));
				params.add(Pair.create("carbon", "off"));
				params.add(Pair.create("hielo", "off"));
				params.add(Pair.create("abarrotes", "off"));
				String apiResponse = conn.sendRequest(getString(R.string.get_botillerias), params, HTTP.UTF_8);		
				Log.d("response", apiResponse);
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("api_response", apiResponse);
				msg.setData(b);
				if(inicial)
					msg.what = INICIAL;
				else
					msg.what = BUSQUEDA;
				mHandler.sendMessage(msg);
			}
		}).run();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{	
		switch(item.getItemId()){
		case R.id.menu_normal:
			mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case R.id.menu_satellite:
			mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case R.id.menu_help:
			Intent i = new Intent(this, Help.class);
			startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void search(View v){
		String busqueda = searchBar.getText().toString();
		if(busqueda!=null && !busqueda.equals(""))
			getBotillerias(busqueda, false);
	}
	
	private String remove(String input) {
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }
	    output = output.trim();
	    return output;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		Log.d("location", "cambio");
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}