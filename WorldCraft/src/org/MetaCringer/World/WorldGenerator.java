package org.MetaCringer.World;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Logger;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.WorldEdit.BicubicInterpolator;
import org.MetaCringer.util.Color;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

public class WorldGenerator extends ChunkGenerator {
	
	PEarthCraft plugin;
	MapManager maps;Logger o;
	Map<Biome,List<Short>> patterns = new HashMap<Biome,List<Short>>();
	private int waterlevel;
	public WorldGenerator(PEarthCraft p,MapManager mm,int WaterLevel) {
		plugin = p; maps = mm;
		o =p.getLogger();
		waterlevel = WaterLevel;
		try {
			patterns = maps.GetPatterns();
		} catch (NumberFormatException | IOException e) {
			
			e.printStackTrace();
			patterns = new HashMap<Biome,List<Short>>();
		}
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		List<BlockPopulator> pop =new ArrayList<BlockPopulator>();
		pop.add(new PopOres(maps));
		pop.add(new PopTrees(maps));
		return pop;
	}
	
	
	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return world.getHighestBlockAt(0, 0).getLocation();
	}
	
	@Override
	public byte[] generate(World world, Random random, int x, int z) {
		return super.generate(world, random, x, z);
	}
	
	@Override
	public short[][] generateExtBlockSections(World world, Random random, int chx, int chz, BiomeGrid biomes) {
		InterpolatorBiome SmootherBiome = new InterpolatorBiome(chx, chz);
		Biome b= null; //= maps.GetBiome(chx,chz);
		
		int[][] heights = new int[5][5];
		double[][] P = new double[4][4];
		short[][] result = new short[256/16][];
		for(int z = 0; z<5;z++) {
			for(int x = 0; x<5;x++) {
				heights[z][x] = maps.getHeight(chx + x-2, chz + z-2);
				
			}
		}
		for(int z = 0; z<4;z++) {
			for(int x = 0; x<4;x++) {
				P[z][x] = (heights[z][x] + heights[z+1][x] +heights[z][x+1] + heights[z+1][x+1])/4;
			}
		}
		BicubicInterpolator Interpolator = null;
		try {
			Interpolator = new BicubicInterpolator(P);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int hy = 0;
		List<Short> pattern;
		
		
		for(int z = 0; z<16;z++) {
			for(int x = 0; x<16;x++) {
				//biomes.setBiome(x, z, b);
				b=SmootherBiome.genBiome(x, z, biomes);
				if(patterns.containsKey(b)) {
					pattern = patterns.get(b);
				}else {
					pattern = new ArrayList<Short>();
					pattern.add((short) Material.GRASS.getId());
				}
				
				hy =  (int) Math.round(Interpolator.interpolate( ((double)z)/16,  ((double)x)/16));
				hy -= pattern.size();
				setblock(result,x,0,z,(short) Material.BEDROCK.getId());
				setblock(result,x,1,z,(short) Material.BEDROCK.getId());
				for(int y = 2; y<hy;y++) {
					setblock(result,x,y,z,(short)Material.STONE.getId());
				}
				for (Short block : pattern) {
					setblock(result,x,hy++,z,block);
				}
				for(int y = waterlevel; y>=hy ; y--) {
					setblock(result,x,y,z,(short)Material.WATER.getId());
				}
			}
		}
		
		
		return result;
	}
	
	
	private void setblock(short[][] result,int x,int y,int z,short blockid) {
		if(result[y >> 4] == null ) {
			result[y>>4] = new short[4096];
		}
		result[y>>4][((y & 0xf) << 8) | (z << 4) | x] = blockid;
	}
/*	
	private void genBiome(int chx, int chz,BiomeGrid biomeMap) throws Exception {
		//Biome[][] biomes = new Biome[5][5];
		Map<Biome,matrix5> biomes=new HashMap<Biome,matrix5>();
		Map<Biome,BicubicInterpolator> Interpolators = new HashMap<Biome,BicubicInterpolator>();
		Biome b= null;
		for(int z = 0; z<5;z++) {
			for(int x = 0; x<5;x++) {
				if(!biomes.containsKey(b = maps.GetBiome(chx + x-2, chz + z-2))) {
					biomes.put(b, new matrix5());
				}
				biomes.get(b).add(chx, chz, 1.0);
			}
		}
		for (Biome biom : biomes.keySet()) {
			Interpolators.put(biom, new BicubicInterpolator(biomes.get(biom).average()));
		}
		
	}
	*/
	private class InterpolatorBiome{
		Map<Biome,BicubicInterpolator> Interpolators = new HashMap<Biome,BicubicInterpolator>();
		public InterpolatorBiome(int chx, int chz) {
			Map<Biome,matrix5> biomes=new HashMap<Biome,matrix5>();
			Biome b= null;
			for(int z = 0; z<5;z++) {
				for(int x = 0; x<5;x++) {
					if(!biomes.containsKey(b = maps.GetBiome(chx + x-2, chz + z-2))) {
						biomes.put(b, new matrix5());
					}
					biomes.get(b).add(x, z, 1.0);
				}
			}
			for (Biome biom : biomes.keySet()) {
				try {
					Interpolators.put(biom, new BicubicInterpolator(biomes.get(biom).average()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		public Biome genBiome(int x,int z,BiomeGrid biomeMap) {
			Biome result = Biome.SKY;
			double value = 0.0,buf=0;
			
			for (Biome biom : Interpolators.keySet()) {
				if(value < (buf =Interpolators.get(biom).interpolate( ((double)x)/16,  ((double)z)/16))) {
					value = buf;
					result = biom;
				}
			}
			biomeMap.setBiome(x, z, result);
			return result;
		}
	}
	private class matrix5{
		private double[][] matrix = new double[5][5];
		protected void add(int x,int z,double value) {
			matrix[x][z] = value;
		}
		protected double[][] average(){
			double[][] result = new double[4][4];
			for(int z = 0; z<4;z++) {
				for(int x = 0; x<4;x++) {
					result[z][x] = (matrix[z][x] + matrix[z+1][x] +matrix[z][x+1] + matrix[z+1][x+1])/4;
				}
			}
			return result;
		}
	}
}
