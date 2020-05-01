package org.MetaCringer.dynmap;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.util.Color;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

public class DynmapManager {
	PEarthCraft p;
	DynmapAPI api;
	MarkerAPI markapi;
	MarkerSet provinces;
	private static DynmapManager instance;
	public static DynmapManager getInstance() {
		return instance;
		
	}
	public DynmapManager(PEarthCraft p,DynmapAPI api) {
		this.p = p;
		this.api = api;
		//while(!api.markerAPIInitialized()) {}
		markapi = api.getMarkerAPI();
		if((provinces = markapi.getMarkerSet("Provinces"))==null) {
			markapi.createMarkerSet("Provinces", "Provinces", null, true).setLabelShow(true);
			markapi.getMarkerSet("Provinces").setHideByDefault(false);
		}
		provinces = markapi.getMarkerSet("Provinces");
		instance = this;
	}
	
	public void paint(String name,String desc,int c,Block from,Block to) {
		double[] xs={from.getX(),to.getX()};
		double[] zs={from.getZ(),to.getZ()};
		AreaMarker m =provinces.createAreaMarker(name, name, true, PEarthCraft.getInstance().getWorld().getName(), xs
		,zs , true);
		
		if(m==null) {p.getLogger().warning("areamarker is null");
		return;}//TODO
		provinces.findAreaMarker(name).setLineStyle(1,0,c);
		provinces.findAreaMarker(name).setFillStyle(0.25,c);
		provinces.findAreaMarker(name).setDescription(desc);
		//p.getLogger().info(m.getCornerX(0) + " "+ m.getCornerZ(0));
		
		
	}
	public AreaMarker getMarker(String name) {
		return provinces.findAreaMarker(name);
	}
	public boolean checkAreaMarker(String name) {
		return provinces.findAreaMarker(name) != null;
	}
	public void deleteMarker(String name) {
		provinces.findAreaMarker(name).deleteMarker();
	}
	public Color getProvinceColor(String id){
		return new Color(provinces.findAreaMarker(id).getFillColor());
	}
	public void resetMarkers() {
		provinces.deleteMarkerSet();
	}
	/*
	 * public static boolean createZoneMarker(String owner,String name,Location l1,Location l2){
		String set="zones";
		String color="CC0000";
		double[] xs={l1.getX(),l2.getX()};
		double[] zs={l1.getZ(),l2.getZ()};
		if(owner.equalsIgnoreCase("Server")){
		set="serverzones";
		color="0000CC";
		}
		api.getMarkerAPI().getMarkerSet(set).createAreaMarker(name, name, true, l1.getWorld().getName(), xs, zs, true);
		api.getMarkerAPI().getMarkerSet(set).findAreaMarker(name).setLabel(name);
		api.getMarkerAPI().getMarkerSet(set).findAreaMarker(name).setLineStyle(0,1,Integer.parseInt(color, 16));
		api.getMarkerAPI().getMarkerSet(set).findAreaMarker(name).setFillStyle(0,Integer.parseInt("DDDDDD", 16));
		api.getMarkerAPI().getMarkerSet(set).findAreaMarker(name).setRangeY(Math.min(l1.getY(), l2.getY()), Math.max(l1.getY(), l2.getY()));
		api.getMarkerAPI().getMarkerSet(set).findAreaMarker(name).setDescription("Name: "+name+"\nBesitzer: "+owner+"\nTyp:"+set.substring(0,set.length()-2)+"\n wtf: "+Math.abs(xs[0]-xs[1])+"x"+Math.abs(zs[0]-zs[1]));
		return true;
}


 if(api.getMarkerAPI().getMarkerSet("zones")==null){
api.getMarkerAPI().createMarkerSet("zones", "zones", null, true).setLabelShow(true);
api.getMarkerAPI().getMarkerSet("zones").setHideByDefault(false);
}
if(api.getMarkerAPI().getMarkerSet("serverzones")==null){
api.getMarkerAPI().createMarkerSet("serverzones", "serverzones", null, true).setLabelShow(true);
api.getMarkerAPI().getMarkerSet("serverzones").setHideByDefault(false);
}
	 * 
	 * 
	 */
	
}
