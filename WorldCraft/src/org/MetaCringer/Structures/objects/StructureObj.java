package org.MetaCringer.Structures.objects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.Structures.PieceStructureObj;
import org.MetaCringer.Structures.patterns.PatternStructure;
import org.MetaCringer.dynmap.DynmapManager;
import org.MetaCringer.util.sql.SQLDatabase;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public abstract class StructureObj {
	public static Map<Integer, StructureObj> registry = new HashMap<Integer, StructureObj>();
	private String Owner;
	private PatternStructure pattern;
	private int x,y,z,chx,chz,id;
	protected StructureObj(int id,int x,int y,int z,PatternStructure pattern,String owner) {
		this.pattern=pattern;
		this.id=id;
		this.x=x;
		this.y=y;
		this.z=z;
		Chunk ch = PEarthCraft.getInstance().getWorld().getBlockAt(x, y, z).getChunk();
		chx = ch.getX();
		chz = ch.getZ();
		Owner = owner;
		
	}
	public int getID() {
		return id;
	}
	/*public static boolean createStructure(int x,int y,int z,String name,String direction,String Owner) throws SQLException {//TODO Зделать проверку что
		// место не занято
		PatternStructure pat = PatternStructure.getStructure(name+direction);
		
		Chunk ch = PEarthCraft.getInstance().getWorld().getBlockAt(x, y, z).getChunk();
		
		SQLDatabase db = PEarthCraft.getInstance().getDB();
		Connection c = db.getConnection();
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery(String.format("SELECT `structureID` FROM chunks WHERE `x`>%d AND `x`<%d AND `z`>%d AND `z`<%d; ", ch.getX(),(ch.getX()+pat.getSizeChX()),ch.getZ(),(ch.getZ()+pat.getSizeChZ())));
		if(r.next()) {return false;}
		s.executeUpdate(String.format("INSERT INTO structures (`x`,`y`,`z`,`name`,`direction`,`owner`) VALUES (%d,%d,%d,'%s','%s','%s')",x,y,z,name,direction,Owner));
		r = s.executeQuery(String.format("SELECT `id` FROM structures WHERE `x`=%d AND `y`=%d AND `z`=%d;",x,y,z ) );
		if(!r.next()) {
			System.out.println("Нет такой структуры, хоть я ее только что создал");
			return false;
		}
		int id = r.getInt("id");
		StructureObj obj = pat.get
		for(int xi=0;xi<pat.getSizeChX();xi++) {
			for(int zi=0;zi<pat.getSizeChZ();zi++) {
				PieceStructureObj pso = new PieceStructureObj(offsetchx, offsetchz, y, ch, sobj)
				s.executeupdate(String.format("INSERT INTO chunks (`cords`,`x`,`y`,`z`,`offsetx`,`offsetz`,`structureID`,) VALUES (%s,%d,%d,%d,%d,%d,%d)\r\n" + 
						"  ON DUPLICATE KEY UPDATE `structureID`=%d,`y`=%d,`offsetx`=%d,`offsetz`=%d;",));// вставить или обновить таблицу chunks 
			}
		}
		s.close();
		c.close();
	}*/
	public void deleteStructure() throws SQLException {//TODO Удалить структу из базы данных и очистить в таблице чанков, чанки связаные с этой структурой
		//DELETE FROM `table` WHERE `number` = номер
		Connection c = PEarthCraft.getInstance().getDB().getConnection();
		Statement s = c.createStatement();
		s.executeUpdate("DELETE FROM `chunks` WHERE `StructureID` = "+ id +";");
		s.executeUpdate("DELETE FROM `structures` WHERE `id` = "+id+";");
		s.close();
		c.close();
		PieceStructureObj pobj;
		for (int x = 0; x < pattern.getSizeChX(); x++) {
			for (int z = 0; z < pattern.getSizeChZ(); z++) {
				if((pobj = PieceStructureObj.registry.remove(chx + ":" + chz)) == null) {continue;}
				pobj.destroyBlocks();
			}
		}
		DynmapManager.getInstance().deleteMarker(id + ":" + pattern.getName());
		StructureObj sobj = registry.remove(id);
		if(sobj != this) {System.out.println("Woops... Не совпали id обьекта с регистром");}
	}
	
	public void build() {//TODO подумать как пошагово строить
		PieceStructureObj pobj;
		for (int x = 0; x < pattern.getSizeChX(); x++) {
			for (int z = 0; z < pattern.getSizeChZ(); z++) {
				pobj = PieceStructureObj.registry.get((chx+x)+":"+(chz+z));
				pobj.build();
				pobj.executeCommands();
			}
		}
	}
	
	public PatternStructure getPattern() {
		return pattern;
	}
}
