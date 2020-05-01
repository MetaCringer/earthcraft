package org.MetaCringer.Structures;

import java.util.HashMap;
import java.util.Map;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.Structures.objects.StructureObj;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class PieceStructureObj {
	public static Map<String, PieceStructureObj> registry = new HashMap<String,PieceStructureObj>();//Карта всех чанков
	protected int offsetchx,chx,y,chz,offsetchz,structureid;
	StructureObj struct;
	PatternPieceStructure chunk;
	
	public int getStructureID() {
		return structureid;
	}
	public static PieceStructureObj getChunk(int chx,int chz) {
		return registry.get(chx+":"+chz);
	}
	private static void putChunk(int chx,int chz,PieceStructureObj p) {
		registry.put(chx+":"+chz, p);
	}
	public int getX() {return chx;}
	public int getZ() {return chz;}
	public PieceStructureObj(int structureid,int offsetchx,int offsetchz,int chx,int y,int chz,PatternPieceStructure ch,StructureObj sobj){
		this.structureid = structureid;
		this.offsetchx =offsetchx;
		this.offsetchz=offsetchz;
		this.y=y;
		this.chx = chx;
		this.chz = chz;
		chunk =ch;
		struct = sobj;
		System.out.println("ch registered:"+this.chx + ":" + this.chz + " id struct: " + struct.getID());
		putChunk(this.chx, this.chz, this);
	}
	public String toString() {
		return struct.getPattern().getName() + ":" + offsetchx + ":" + y + ":" + offsetchz;
	}
	@SuppressWarnings("deprecation")
	public void destroyBlocks() {
		Chunk ch = PEarthCraft.getInstance().getWorld().getChunkAt(chx, chz);
		for(int xi=0;xi<16;xi++) {
			for(int zi=0;zi<16;zi++) {
				for(int yi=0;yi< chunk.sizey;yi++) {
					if(!chunk.canBreak(xi, yi, zi)) {
						ch.getBlock(xi, yi+y, zi).setTypeIdAndData(0, (byte) 0, true);
					}
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	public void build() {
		Chunk ch = PEarthCraft.getInstance().getWorld().getChunkAt(chx, chz);
		for(int xi=0;xi<16;xi++) {
			for(int zi=0;zi<16;zi++) {
				for(int yi=0;yi< chunk.sizey;yi++) {
					ch.getBlock(xi, yi+y, zi).setTypeIdAndData(chunk.blocksid[xi][zi][yi], chunk.blocksdata[xi][zi][yi], false);
				}
			}
		}
	}
	//public boolean isChunkLoaded() {return PEarthCraft.getInstance().getWorld().getChunkAt(chx, chz).isLoaded();}
	public void executeCommands() {
		Chunk ch = PEarthCraft.getInstance().getWorld().getChunkAt(chx, chz);
		String args[];
		for (String cord : chunk.commands.keySet()) {
			args = cord.split(":");
			Block b = ch.getBlock(Integer.parseInt(args[0]), Integer.parseInt(args[1])+y, Integer.parseInt(args[2]));
			chunk.struct.executeCommand(b, chunk.commands.get(cord));
		}
	}
	public boolean canBreak(int x,int y, int z) {
		if(y>(chunk.sizey+this.y) || y < this.y) {
			return true;
		}
		int xi=(x<0)? (15 - Math.abs((x+1)%16)): x%16,yi=y-this.y,zi=(z<0)?(15-Math.abs((z+1)%16)):z%16;
		System.out.println("Попытка сломать на "+ xi + ":" + yi +":" +zi + " по шаблону там "+ chunk.blocksdata[xi][zi][yi] +" " + chunk.blocksid[xi][zi][yi]);
		return chunk.blocksdata[xi][zi][yi] ==0 && chunk.blocksid[xi][zi][yi] == 0;
	}
}
