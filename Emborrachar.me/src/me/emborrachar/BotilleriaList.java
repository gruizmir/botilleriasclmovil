package me.emborrachar;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class BotilleriaList {
	private ArrayList<Botilleria> lista;
	protected static JSONArray jsonArray;

	public int count(){
		if(lista!=null)
			return lista.size();
		else 
			return 0;
	}
	
	public BotilleriaList(){
		lista = new ArrayList<Botilleria>();
	}

	public Botilleria get(int index){
		try{
			return lista.get(index);
		}catch(IndexOutOfBoundsException e){
			return null;
		}
	}

	public void parseList(String apiResponse){
		JSONObject jsonObject = null;
		try {
			jsonArray = new JSONArray(apiResponse);
			for(int i=0; i<jsonArray.length(); i++){
				Botilleria bot = new Botilleria();
				jsonObject = jsonArray.getJSONObject(i);
				bot.setName(jsonObject.getString("nombre"));
				bot.setAddress(jsonObject.getString("calle") + " " + jsonObject.getString("numero_calle"));
				bot.setTime1(jsonObject.getString("horario_desde1") + " a " + jsonObject.getString("horario_hasta1"));
				bot.setTime2(jsonObject.getString("horario_desde2") + " a " + jsonObject.getString("horario_hasta2"));
				bot.setLatitud(Double.parseDouble(jsonObject.getString("latitud")));
				bot.setLongitud(Double.parseDouble(jsonObject.getString("longitud")));
				if(jsonObject.getInt("hielo")==1)
					bot.setIce(true);
				else
					bot.setIce(false);

				if(jsonObject.getInt("carbon")==1)
					bot.setCharcoal(true);
				else
					bot.setCharcoal(false);

				if(jsonObject.getInt("abarrotes")==1)
					bot.setFood(true);
				else
					bot.setFood(false);
				lista.add(bot);
			}
		} catch (JSONException e) {
			Log.e("json_fail", e.getMessage());
		}
	}
}
