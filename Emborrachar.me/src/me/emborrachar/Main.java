package me.emborrachar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.getUiSettings().setZoomControlsEnabled(false);
		mapa.setInfoWindowAdapter(new BorrachoAdapter(this));
		showStores();
		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {
				return false;
			}
		});
		CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(-33.039983,-71.592681), (float)15.0);
		mapa.moveCamera(cam);
	}

	private void showStores(){
		mapa.addMarker(new MarkerOptions()
		.position(new LatLng(-33.039983,-71.592681))
		.title("San Pedro")
		.snippet("Agua santa 24\n" +  "Lun a Jue: " + "11.00 a 01.00" + " hrs\n" +
				"Vie a Dom: " + "11.00 a 03.00" + " hrs\nYES\nYES\nNO")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
		}
		return super.onOptionsItemSelected(item);
	}

	class BorrachoAdapter implements GoogleMap.InfoWindowAdapter{
		private LayoutInflater inf;
		public BorrachoAdapter(Context cont){
			inf = LayoutInflater.from(cont);
		}

		@Override
		public View getInfoContents(Marker marker) {
			return null;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			View mView = inf.inflate(R.layout.balloon, null); 
			TextView botName = (TextView) mView.findViewById(R.id.bot_name);
			TextView botAddress = (TextView) mView.findViewById(R.id.bot_address);
			TextView botHorario1 = (TextView) mView.findViewById(R.id.bot_horario1);
			TextView botHorario2 = (TextView) mView.findViewById(R.id.bot_horario2);
			ImageView botFood= (ImageView) mView.findViewById(R.id.bot_food);
			ImageView botCoal= (ImageView) mView.findViewById(R.id.bot_coal);
			botName.setText(marker.getTitle());
			String[] values = marker.getSnippet().split("\n");
			botAddress.setText(values[0]);
			botHorario1.setText(values[1]);
			botHorario2.setText(values[2]);
			if(values[3].equals("YES")){
				botFood.setImageResource(R.drawable.food);
			}
			if(values[4].equals("YES")){
				botCoal.setImageResource(R.drawable.coal);
			}
			return mView;
		}

	}

}
