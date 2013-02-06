package me.emborrachar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class BorrachoAdapter implements GoogleMap.InfoWindowAdapter{
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
		ImageView botIce= (ImageView) mView.findViewById(R.id.bot_ice);
		botName.setText(marker.getTitle());
		if(marker.getSnippet()!= null && !marker.getSnippet().equals("")){
			try{
				String[] values = marker.getSnippet().split("\n");
				botAddress.setText(values[0]);
				botHorario1.setText(values[1]);
				botHorario2.setText(values[2]);
				if(values[3].equals("true")){
					botFood.setImageResource(R.drawable.food);
				}
				else{
					botFood.setMaxWidth(0);
				}
				if(values[4].equals("true")){
					botCoal.setImageResource(R.drawable.coal);
				}
				else{
					botCoal.setMaxWidth(0);
				}
				if(values[5].equals("true")){
					botIce.setImageResource(R.drawable.ice);
				}else{
					botIce.setMaxWidth(0);
				}
			}catch(NullPointerException e){

			}
		}
		else{
			botAddress.setHeight(0);
			botHorario1.setHeight(0);
			botHorario2.setHeight(0);
			botFood.setMaxWidth(0);
			botIce.setMaxWidth(0);
			botCoal.setMaxWidth(0);
		}
		return mView;
	}
}
