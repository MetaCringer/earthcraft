package org.MetaCringer.World;

import java.util.Map;
import java.util.Random;


import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class PopOres extends BlockPopulator {
	MapManager m;
	public PopOres(MapManager manager) {
		m = manager;
	}
	@Override
	public void populate(World world, Random rand, Chunk ch) {
		Map<Material, Double> chance = m.getChanceOreSpawn(ch.getX(), ch.getZ());
		double r,sum=0;
		
		
		for(int x=0;x<16;x++) {
			for(int z=0;z<16;z++) {
				for(int y = 0;y<64;y++) {
					
					if(Material.STONE.equals(ch.getBlock(x, y, z).getType())) {
						r = rand.nextDouble()*3;
						sum=0;
						for (Material mat : chance.keySet()) {
							
							if((sum+= chance.get(mat))>r) {
								ch.getBlock(x, y, z).setType(mat);
								break;
							}
						}	
					}
					
				}
			}
		}
		
		
		
	}
	
	
}
