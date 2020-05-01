package org.MetaCringer.Structures;

import java.util.HashMap;
import java.util.Map;

import org.MetaCringer.Structures.patterns.PatternStructure;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class PatternPieceStructure {
	PatternStructure struct;
	public short[][][] blocksid;// = new short[16][16][];
	public byte[][][] blocksdata;// = new byte[16][16][];
	public Map<String, String> commands=new HashMap<String, String>();
	int sizey;
	public PatternPieceStructure(PatternStructure structure,int Height) {
		struct = structure;
		sizey = Height;
		blocksid = new short[16][16][sizey];
		blocksdata = new byte[16][16][sizey];
		
		//Bukkit.getWorlds().get(0).getBlockAt(0, 0, 0).
	}
	
	private int getHeight() {
		return sizey;
	}
	
	public boolean canBreak(int x,int y, int z) {
		if(y>sizey || y < 0) {
			return true;
		}
		return !(blocksdata[x][z][y] !=0 || blocksid[x][z][y]!= 0);
	}
	/*@SuppressWarnings("deprecation")
	public void build(Chunk ch,int height) {
		for (int x = 0; x < 16; x++) {
			for(int z=0;z<16;z++) {
				for(int y = 0;y<sizey;y++) {
					ch.getBlock(x, height+y, z).setTypeIdAndData(blocksid[x][z][y], blocksdata[x][z][y], false);
				}
			}
		}
		for (String b : commands.keySet()) {
			struct.executeCommand(ch.getBlock(Integer.parseInt(b.split(":")[0]), Integer.parseInt(b.split(":")[1]), Integer.parseInt(b.split(":")[2])), commands.get(b));
		}
		
	}*/
	
	
}
