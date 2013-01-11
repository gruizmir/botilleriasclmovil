package com.emborrachar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Main extends MapActivity implements LocationListener {
	//DEBUG KEY: 0f_ElE3aFaKxdvr6FJdWOufjRqttokcx6hLuAVg
	private int RADIO=2000;	//radio en metros de busqueda
	private String bestProvider;
	private LocationManager locationManager;
	private SlidingDrawer slide;
	private TextView botName;
	private TextView botAddress;
	private TextView botHorario1;
	private TextView botHorario2;
	private ImageView botFood;
	private ImageView botCoal;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		slide = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		botName = (TextView) findViewById(R.id.bot_name);
		botAddress = (TextView) findViewById(R.id.bot_address);
		botHorario1 = (TextView) findViewById(R.id.bot_horario1);
		botHorario2 = (TextView) findViewById(R.id.bot_horario2);
		botHorario2 = (TextView) findViewById(R.id.bot_horario2);
		botFood= (ImageView) findViewById(R.id.bot_food);
		botCoal= (ImageView) findViewById(R.id.bot_coal);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		//habilitamos el control de zoom
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		try{
			Criteria criteria = new Criteria();
			bestProvider = locationManager.getBestProvider(criteria, false);
			Location location = locationManager.getLastKnownLocation(bestProvider);
			updateLocation(location);

			//				updateLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
			/* se actualizará cada minuto y 50 metros de cambio en la localización
           mientras más pequeños sean estos valores más frecuentes serán las actualizaciones */
			//			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 50, this);
		}catch(NullPointerException e){

		}
	}

	private void showData(){

	}

	@Override
	protected void onPause(){
		super.onPause();
		if(locationManager!=null)
			locationManager.removeUpdates(this);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(locationManager!=null)
			locationManager.removeUpdates(this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		updateLocation(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}	

	@Override
	public void onProviderEnabled(String provider) {}

	protected void updateLocation(Location location){
		MapView mapView = (MapView) findViewById(R.id.mapview);
		MapController mapController = mapView.getController();
		GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
		mapController.animateTo(point);        
		mapController.setZoom(15);
		//        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		//        try {
		//            List<Address> addresses = geoCoder.getFromLocation(
		//                point.getLatitudeE6()  / 1E6, 
		//                point.getLongitudeE6() / 1E6, 1);
		//            String address = "";
		//            if (addresses.size() > 0) {
		//                for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
		//                   address += addresses.get(0).getAddressLine(i) + "\n";
		//            }
		//        }
		//        catch (IOException e) {                
		//            e.printStackTrace();
		//        }           
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
		MyOverlay itemizedoverlay = new MyOverlay(drawable, this);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
		//        MyOverlay marker = new MyOverlay(point);
		//        mapOverlays.add(marker);
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		mapView.invalidate();	
	}

	@Override
	/**
	 * Accion cuando el servicio de GPS no esta encendido. 
	 */
	public void onProviderDisabled(String provider) {
		Intent intent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}

	public class MyOverlay extends ItemizedOverlay{
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private Context mContext; 
		public MyOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}

		public MyOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mContext = context;
		}

		public void addOverlay(OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}

		@Override
		protected boolean onTap(int index) {
			//		  OverlayItem item = mOverlays.get(index);
			//		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			//		  dialog.setTitle(item.getTitle());
			//		  dialog.setMessage(item.getSnippet());
			//		  dialog.show();
			//			//Accion de mostrar datos de botilleria
			botName.setText("San Pedro");
			botAddress.setText("Agua santa 24");
			botHorario1.setText("Lun a Jue: " + "11.00 a 01.00" + " hrs");
			botHorario2.setText("Vie a Dom: " + "11.00 a 03.00" + " hrs");
			slide.animateToggle();
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}