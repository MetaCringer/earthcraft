package org.MetaCringer.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.MetaCringer.Main.PEarthCraft;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

public class OldWorldGenerator extends ChunkGenerator {
	/*
	PWorldCraft plugin;
	MapManager maps;Logger o;
	public OldWorldGenerator(PWorldCraft p,MapManager mm) {
		plugin = p; maps = mm;
		o =p.getLogger();
	}
	
	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return world.getHighestBlockAt(0, 0).getLocation();
	}
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		List<BlockPopulator> pop =new ArrayList<BlockPopulator>();
		//pop.add(new D);
		return pop;
	}
	@Override
	public byte[] generate(World world, Random random, int x, int z) {
		return super.generate(world, random, x, z);
	}
	@Override
	public byte[][] generateBlockSections(World world, Random random, int chx, int chz, BiomeGrid biomes) {
		Biome b = maps.GetBiome(chx, chz);
		NeighborgsMetaData neighbors = new NeighborgsMetaData(chx, chz,maps);
		for(int x =0;x<16;x++) {
			for(int z=0;z<16;z++) {
				biomes.setBiome(x, z, b);
			}	
		}
		return super.generateBlockSections(world, random, chx, chz, biomes);
	}
	
	@Override
	public short[][] generateExtBlockSections(World world, Random random, int chx, int chz, BiomeGrid biomes) {
		Biome b = maps.GetBiome(chx,chz);
		NeighborgsMetaData neighbors = new NeighborgsMetaData(chx,chz,maps);
		short[][] data= new short[256/16][];
		
				switch (b) {
				case PLAINS:{
					o.info("gen "+ chx +" "+ chz + Biome.PLAINS);
					GenPlains(data,neighbors,world,biomes);
					break;
				}
				case FOREST:{
					o.info("gen "+ chx +" "+ chz + Biome.FOREST);
					GenForest(data,neighbors,world,biomes);
					break;
				}
				case OCEAN:{
					o.info("gen "+ chx +" "+ chz + Biome.OCEAN);
					if((neighbors.getAmount(Biome.OCEAN) + neighbors.getAmount(Biome.SKY)) == 8) {
						GenOcean(data,neighbors,world,biomes);
					}else {
						GenBeach(data,neighbors,world,biomes);
					}
					break;
				}
				case TAIGA:{
					o.info("gen "+ chx +" "+ chz + Biome.TAIGA);
					GenTaiga(data,neighbors,world,biomes);
					break;
				}
				case DESERT:{
					GenDesert(data,neighbors,world,biomes);
					break;
				}
				case ICE_FLATS:{
					GenIceFlat(data,neighbors,world,biomes);
					break;
				}
				case TAIGA_COLD:{
					GenCTaiga(data,neighbors,world,biomes);
					break;
				}
				case SAVANNA:{
					GenSavanna(data,neighbors,world,biomes);
					break;
				}
				case EXTREME_HILLS:{
					if((neighbors.getAmount(Biome.EXTREME_HILLS) + 
							neighbors.getAmount(Biome.ICE_FLATS) + 
							neighbors.getAmount(Biome.TAIGA_COLD)) ==8) {
						GenIceMountains(data,neighbors,world,biomes);
					}else {
						GenExtremeHills(data,neighbors,world,biomes);
					}
					break;
				}
				case JUNGLE:{
					GenJungle(data,neighbors,world,biomes);
					break;
				}
				default:
					o.info("gen " + chx +" "+ chz + Biome.SKY);
					GenSky(biomes);
					break;
				}
				
				
		
		return data;
	}
	
	private void GenJungle(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		GenLand(data, biomes, Biome.JUNGLE);
		
	}

	private void GenExtremeHills(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		GenLand(data, biomes, Biome.EXTREME_HILLS);
		genMontain(data, neighbors, world);
	}

	
	private void GenIceMountains(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		GenLand(data, biomes, Biome.ICE_MOUNTAINS);
		genMontain(data, neighbors, world);
	}
	private void genMontain(short[][]data,NeighborgsMetaData neighbors,World world) {
		autofor(data, 4, 12, 4, 12, 60, 100, Material.STONE);
		if(neighbors.getNeigbor(NeighborgsMetaData.n).equals(Biome.EXTREME_HILLS)) {
			autofor(data, 4, 12, 0, 4, 60, 98, Material.STONE);
		}
		if(neighbors.getNeigbor(NeighborgsMetaData.e).equals(Biome.EXTREME_HILLS)) {
			autofor(data, 12, 16, 4, 12, 60, 98, Material.STONE);
		}
		if(neighbors.getNeigbor(NeighborgsMetaData.s).equals(Biome.EXTREME_HILLS)) {
			autofor(data, 4, 12, 12, 16, 60, 98, Material.STONE);
		}
		if(neighbors.getNeigbor(NeighborgsMetaData.w).equals(Biome.EXTREME_HILLS)) {
			autofor(data, 0, 4, 4, 12, 60, 98, Material.STONE);
		}
		
		
		if(neighbors.getNeigbor(NeighborgsMetaData.n).equals(Biome.EXTREME_HILLS) &&
				neighbors.getNeigbor(NeighborgsMetaData.e).equals(Biome.EXTREME_HILLS)) {
			autofor(data, 12, 16, 0, 4, 60, 96, Material.STONE);
		}
		if(neighbors.getNeigbor(NeighborgsMetaData.e).equals(Biome.EXTREME_HILLS) &&
				neighbors.getNeigbor(NeighborgsMetaData.s).equals(Biome.EXTREME_HILLS)) {
			autofor(data, 12, 16, 12, 16, 60, 96, Material.STONE);
		}
		if(neighbors.getNeigbor(NeighborgsMetaData.s).equals(Biome.EXTREME_HILLS) &&
				neighbors.getNeigbor(NeighborgsMetaData.w).equals(Biome.EXTREME_HILLS)) {
			autofor(data, 0, 4, 12, 16, 60, 96, Material.STONE);
		}
		if(neighbors.getNeigbor(NeighborgsMetaData.w).equals(Biome.EXTREME_HILLS) &&
				neighbors.getNeigbor(NeighborgsMetaData.n).equals(Biome.EXTREME_HILLS)) {
			autofor(data, 0, 4, 0, 4, 60, 96, Material.STONE);
		}
		//TODO
		
	}

	private void GenSavanna(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		GenLand(data, biomes, Biome.SAVANNA);
		
	}

	private void GenCTaiga(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		GenLand(data, biomes, Biome.TAIGA_COLD);
		
	}

	private void GenIceFlat(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		GenLand(data, biomes, Biome.ICE_FLATS);
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				setblock(data, x, 63, z, (short)Material.PACKED_ICE.getId());
				setblock(data, x, 64, z, (short)Material.SNOW_BLOCK.getId());
			}
		}
	}

	private void GenDesert(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				biomes.setBiome(x, z, Biome.DESERT);
				setblock(data, x, 0, z,(short) Material.BEDROCK.getId());
				for (int y = 1; y < 50; y++) {
					setblock(data, x, y, z, (short)Material.STONE.getId());
				}
				for (int y = 50; y < 60; y++) {
					setblock(data, x, y, z, (short)Material.SANDSTONE.getId());
				}
				for (int y = 60; y < 65; y++) {
					setblock(data, x, y, z, (short) Material.SAND.getId());
				}
				
			}
		}
		
	}

	private void GenTaiga(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		GenLand(data, biomes, Biome.TAIGA);
		
	}

	private void GenBeach(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				biomes.setBiome(x, z, Biome.BEACHES);
				setblock(data, x, 0, z,(short) Material.BEDROCK.getId());
				for (int y = 1; y < 60; y++) {
					setblock(data, x, y, z, (short)Material.STONE.getId());
				}
				for (int y = 60; y < 64; y++) {
					setblock(data, x, y, z, (short)Material.WATER.getId());
				}
			}
		}
		if(!neighbors.getNeigbor(neighbors.n).equals(Biome.OCEAN)) {
			//autofor(data, 0, 16, 0, 4, 50, 64, Material.WATER);
			autofor(data, 0, 16, 0, 4, 60, 65, Material.SAND);
		}
			
		if(!neighbors.getNeigbor(neighbors.e).equals(Biome.OCEAN)) {
			//autofor(data, 12, 16, 0, 16, 50, 64, Material.WATER);
			autofor(data, 12, 16, 0, 16, 60, 65, Material.SAND);
		}
			
		if(!neighbors.getNeigbor(neighbors.s).equals(Biome.OCEAN)) {
			//autofor(data, 0, 16, 12, 16, 50, 64, Material.WATER);
			autofor(data, 0, 16, 12, 16, 60, 65, Material.SAND);
		}
		if(!neighbors.getNeigbor(neighbors.w).equals(Biome.OCEAN)) {
			//autofor(data, 0, 4, 0, 16, 50, 64, Material.WATER);
			autofor(data, 0, 4, 0, 16, 60, 65, Material.SAND);
		}
	}
	
	private void autofor (short[][] data,int xinit,int xend,int zinit,int zend,int yinit,int yend,Material m){
		for(int x =xinit;x<xend;x++) {
			for (int z = zinit; z < zend; z++) {
				for(int y=yinit;y<yend;y++) {
					setblock(data, x, y, z, (short)m.getId());
				}
			}
		}
	}
	
	private void GenOcean(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				biomes.setBiome(x, z, Biome.OCEAN);
				setblock(data, x, 0, z,(short) Material.BEDROCK.getId());
				for (int y = 1; y < 30; y++) {
					setblock(data, x, y, z, (short)Material.STONE.getId());
				}
				for (int y = 30; y < 64; y++) {
					setblock(data, x, y, z, (short)Material.WATER.getId());
				}
			}
		}
		
	}

	private void GenForest(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		GenLand(data, biomes, Biome.FOREST);
		
	}

	private void GenPlains(short[][] data, NeighborgsMetaData neighbors, World world, BiomeGrid biomes) {
		GenLand(data, biomes, Biome.PLAINS);
	}
	private void GenLand(short[][]data,BiomeGrid biomes,Biome b) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				biomes.setBiome(x, z, b);
				setblock(data, x, 0, z,(short) Material.BEDROCK.getId());
				for (int y = 1; y < 60; y++) {
					setblock(data, x, y, z, (short)Material.STONE.getId());
				}
				for (int y = 60; y < 64; y++) {
					setblock(data, x, y, z, (short)Material.DIRT.getId());
				}
				setblock(data, x, 64, z, (short) Material.GRASS.getId());
			}
		}
	}

	private void GenSky(BiomeGrid biomes) {
		for (int x = 0; x < 16; x++) {
			for(int z =0;z<16;z++) {
				biomes.setBiome(x, z, Biome.SKY);
			}
		}
		
	}

	private void setblock(byte[][] result,int x,int y,int z,byte blockid) {
		if(result[y >> 4] == null ) {
			result[y>>4] = new byte[4096];
		}
		result[y>>4][((y & 0xf) << 8) | (z << 4) | x] = blockid;
	}
	private void setblock(short[][] result,int x,int y,int z,short blockid) {
		if(result[y >> 4] == null ) {
			result[y>>4] = new short[4096];
		}
		result[y>>4][((y & 0xf) << 8) | (z << 4) | x] = blockid;
	}
	*/
}
