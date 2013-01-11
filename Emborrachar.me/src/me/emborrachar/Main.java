package me.emborrachar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main extends android.support.v4.app.FragmentActivity {
	private GoogleMap mapa;
	private int RADIO=2000;	//radio en metros de busqueda
	private SlidingDrawer slide;
	private TextView botName;
	private TextView botAddress;
	private TextView botHorario1;
	private TextView botHorario2;
	private ImageView botFood;
	private ImageView botCoal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		slide = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		botName = (TextView) findViewById(R.id.bot_name);
		botAddress = (TextView) findViewById(R.id.bot_address);
		botHorario1 = (TextView) findViewById(R.id.bot_horario1);
		botHorario2 = (TextView) findViewById(R.id.bot_horario2);
		botHorario2 = (TextView) findViewById(R.id.bot_horario2);
		botFood= (ImageView) findViewById(R.id.bot_food);
		botCoal= (ImageView) findViewById(R.id.bot_coal);
		
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.getUiSettings().setZoomControlsEnabled(false);
		showStores();
		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {
				botName.setText("San Pedro");
				botAddress.setText("Agua santa 24");
				botHorario1.setText("Lun a Jue: " + "11.00 a 01.00" + " hrs");
				botHorario2.setText("Vie a Dom: " + "11.00 a 03.00" + " hrs");
				slide.animateToggle();
				return false;
			}
		});
		CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(-33.039983,-71.592681), (float)15.0);
		mapa.moveCamera(cam);
	}

	private void showStores(){
		mapa.addMarker(new MarkerOptions()
		.position(new LatLng(-33.039983,-71.592681))
		.title("Cachorritos House")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
		}
		return super.onOptionsItemSelected(item);
	}

}
