package org.MetaCringer.World;

import java.util.Map;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class PopTrees extends BlockPopulator {
	private double chance = 0.1;
	MapManager map;
	public PopTrees(MapManager map) {
		this.map = map;
	}
	
	@Override
	public void populate(World world, Random rand, Chunk ch) {
		double chance=0;
		TreeType type = null;
		switch (map.GetBiome(ch.getX(),ch.getZ()).toString()) {
			case "PLAINS":{
				type = TreeType.TREE;
				chance = 1.0/8.0;
				break;
			}
			case "JUNGLE":{
				type = TreeType.JUNGLE;
				chance = 1.0/4.0;
				break;
			}
			case "FOREST":{
				type = TreeType.BIG_TREE;
				chance = 1.0/4.0;
				break;
			}
			case "TAIGA":{
				type = TreeType.BIG_TREE;
				chance = 1.0/8.0;
				break;
			}
			case "SAVANNA":{
				type = TreeType.ACACIA;
				chance = 1.0/8.0;
				break;
			}
			default:
				break;
			
		}
		if(chance == 0) {return;}
		Location l;
		for(int z = 0;z<16;z++) {
			for (int x = 0; x < 16; x++) {
				if(rand.nextDouble() < chance) {
					l=ch.getBlock(x, 0, z).getLocation();
					l.setY(world.getHighestBlockYAt(l.getBlockX(), l.getBlockZ())+1);
					world.generateTree(l, type);
				}
			}
		}
		//world.generateTree(arg0, type);
		
	}
	
}
