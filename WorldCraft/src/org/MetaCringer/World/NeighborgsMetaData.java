package org.MetaCringer.World;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Biome;

public class NeighborgsMetaData {
	private Biome[] nbs=new Biome[8];
	private Map<Biome, Integer> amountBiomes = new HashMap<Biome, Integer>();
	public static final int n=0,ne=1,e=2,se=3,s=4,sw=5,w=6,nw=7;
	public NeighborgsMetaData(int chx,int chz,MapManager maps) { // TODO Отпала нужда следить за соседами так как 
		nbs[n]=maps.GetBiome(chx, chz-1); // появилась карта высот
		nbs[ne]=maps.GetBiome(chx+1, chz-1);
		nbs[e]=maps.GetBiome(chx+1, chz);
		nbs[se]=maps.GetBiome(chx+1, chz+1);
		nbs[s]=maps.GetBiome(chx, chz+1);
		nbs[sw]=maps.GetBiome(chx-1, chz+1);
		nbs[w]=maps.GetBiome(chx-1, chz);
		nbs[nw]=maps.GetBiome(chx-1, chz-1);
		for (Biome biome : nbs) {
			Integer value = amountBiomes.get(biome);
			if (value ==null) value = 1; else value = value+1;
			amountBiomes.put(biome,value);
		}
	}
	
	public Biome getNeigbor(int direction) {
		return nbs[direction];
	}
	
	public int getAmount(Biome b){
		Integer result=0;
		if((result = amountBiomes.get(b)) == null) {
			return 0;
		}
		return result;
	}
	
	
}
