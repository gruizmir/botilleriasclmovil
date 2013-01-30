package me.emborrachar;

public class Botilleria {
	private String name;
	private String address;
	private String city;
	private String country;
	private boolean ice;
	private boolean charcoal;
	private boolean food;
	private String time1;
	private String time2;
	private double latitud;
	private double longitud;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public boolean hasIce() {
		return ice;
	}
	public void setIce(boolean ice) {
		this.ice = ice;
	}
	public boolean hasCharcoal() {
		return charcoal;
	}
	public void setCharcoal(boolean charcoal) {
		this.charcoal = charcoal;
	}
	public boolean hasFood() {
		return food;
	}
	public void setFood(boolean food) {
		this.food = food;
	}
	public String getTime1() {
		return time1;
	}
	public void setTime1(String time1) {
		this.time1 = time1;
	}
	public String getTime2() {
		return time2;
	}
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
	public String getSnippet(){
		String resp = getAddress() + "\n";
		if(getTime1()!=null && !getTime1().equals("null") && !getTime1().equals("null a null"))
			resp = resp + "Dom a Jue: " + getTime1() + " hrs";
		else 
			resp = resp + "Sin horario disponible";
		resp = resp + "\n";
		if(getTime2()!=null && !getTime2().equals("null") && !getTime2().equals("null a null"))
			resp = resp  + "Vie a Sab: " + getTime2() + " hrs";
		resp = resp + "\n";
		resp = resp + Boolean.toString(hasFood()) + "\n" + Boolean.toString(hasCharcoal()) + "\n" + Boolean.toString(hasIce());
		return resp;
	}
}
