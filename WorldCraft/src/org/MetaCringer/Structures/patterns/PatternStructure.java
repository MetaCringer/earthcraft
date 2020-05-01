package org.MetaCringer.Structures.patterns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.Structures.PatternPieceStructure;
import org.MetaCringer.Structures.PieceStructureObj;
import org.MetaCringer.Structures.objects.StructureObj;
import org.MetaCringer.dynmap.DynmapManager;
import org.MetaCringer.util.sql.SQLDatabase;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

public abstract class PatternStructure {
	protected static Map<String, PatternStructure> registry=new HashMap<String, PatternStructure>();
	private String name,direction;
	private int sizex,sizey,sizez;
	private int sizechx,sizechz;
	boolean tiedToGrid;
	private PatternPieceStructure[][] struct;//this
	//private short[][][] blocksid;//or this
	//private short[][][] blocksdata;
	//private Map<String,String> commands;//=new HashMap<String,String>();
	protected PatternStructure(String name, int sizex, int sizey, int sizez) {
		int i =name.lastIndexOf("_");
		this.name= name.substring(0, i);
		direction = name.substring(i);
		this.sizex=sizex;
		this.sizey=sizey;
		this.sizez=sizez;
		sizechx = sizex/16;
		sizechz = sizez/16;
		tiedToGrid = ((sizex%16) == 0) && ((sizez%16)==0);
		struct = new PatternPieceStructure[sizechx][sizechz];
	}
	public String getName() {return name;}
	public String getDirection() {return direction;}
	public int getSizeChX() {return sizechx;}
	public int getSizeChZ() {return sizechz;}
	public PatternPieceStructure getPChunk(int chx,int chz) {return struct[chx][chz];}
	public static PatternStructure getStructure(String name) {
		if(registry.containsKey(name)) {
			return registry.get(name);
		}else {
			return null;
		}
	}
	public static boolean loadStructure(String name,String direction,String theme) throws IOException {
		File f = new File(PEarthCraft.getInstance().getDataFolder(),"assets/templates/themes/"+theme +"/structures/"+name+"/"+name+direction +".def");
		System.out.println(f);
		if(!f.exists()) {
			return false;
		}
		
		
		
		String l;
		String[] args,cords;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		l=br.readLine();
		int sizex = Integer.parseInt(l.split(";")[0]);
		int sizey = Integer.parseInt(l.split(";")[1]);
		int sizez = Integer.parseInt(l.split(";")[2]);
		PatternStructure s;
		switch (name) {
		case "monument":{
			s = new PatDefault(name+direction,sizex,sizey,sizez);
			break;
		}
		default:
			s = new PatDefault(name+direction,sizex,sizey,sizez);
		}
		registry.put(s.name+s.direction, s);
		int x,y,z;
		if(s.tiedToGrid) {
			s.struct = new PatternPieceStructure[s.sizechx][];
			for(int xi = 0; xi < s.sizechx;xi++) {
				s.struct[xi] = new PatternPieceStructure[s.sizechz];
				for (int zi = 0; zi < s.sizechz; zi++) {
					s.struct[xi][zi] = new PatternPieceStructure(s,s.sizey);
				}
			}
			short id;
			byte data;
			String cmd;
			while(br.ready()) {
				args=br.readLine().split(",");
				cords = args[0].split(":");
				x=Integer.parseInt(cords[0]);
				y=Integer.parseInt(cords[1]);
				z=Integer.parseInt(cords[2]);
				id = Short.parseShort(args[1].split(":")[0]);
				data = Byte.parseByte( args[1].split(":")[1]);
				cmd="";
				if(((id == 68) || (id == 63)) && (args.length > 2)) {
					System.out.println(args[0] + args[1] + "  "+args[2]);
					for(int i = 2;i<args.length;i++) {
						cmd += args[i] +",";
					}
					cmd = cmd.substring(0, cmd.length()-1);
							
					s.struct[x/16][z/16].commands.put((x%16) + ":" + y + ":" + (z%16) , cmd);
					System.out.println((x%16) + ":" + y + ":" + (z%16) + ":" + cmd);
					//s.struct[x/16][z/16].blocksdata[x%16][z%16][y] =  data;
					//continue;
				}
				s.struct[x/16][z/16].blocksid[x%16][z%16][y] = id;
				s.struct[x/16][z/16].blocksdata[x%16][z%16][y] = data;
			}
			
		}else {
			//TODO
			System.out.println("Здание не по сетке");
			registry.remove(s.name+s.direction);
			br.close();
			return false;
			/*
			s.commands = new HashMap<String,String>();
			while(br.ready()) {
				args=br.readLine().split(",");
				cords = args[0].split(":");
				x=Integer.parseInt(cords[0]);
				y=Integer.parseInt(cords[1]);
				z=Integer.parseInt(cords[2]);
				if((Integer.parseInt(args[1].split(":")[0]) == 68) && args.length >= 6) {
					
					s.commands.put(x + ":" + y + ":" + z , args[2]+","+args[3]+","+args[4]+","+args[5]);
					continue;
				}
				s.blocksid[x][z][y] = Short.parseShort( args[1].split(":")[0] );
				s.blocksdata[x][z][y] = Byte.parseByte( args[1].split(":")[1] );
			}
			*/
		}
		br.close();
		
		return true;
		
	}
	public StructureObj createStructureObj(int id,int x,int y,int z,String Owner) {
		StructureObj obj = createObj(id,x,y,z,this,Owner);
		StructureObj.registry.put(obj.getID(), obj);
		Chunk ch = PEarthCraft.getInstance().getWorld().getBlockAt(x, y, z).getChunk();
		for(int xi=0;xi< getSizeChX();xi++) {
			for(int zi=0;zi< getSizeChZ();zi++) {
				new PieceStructureObj(id,xi,zi,ch.getX() + xi,y,ch.getZ() + zi,  struct[xi][zi], obj);
			}
		}
		return obj;
	}
	public boolean createStructureObjWithDB(int x,int y,int z,String Owner) throws SQLException {// Создание обьекта структуры
		Chunk ch = PEarthCraft.getInstance().getWorld().getBlockAt(x, y, z).getChunk();
		
		SQLDatabase db = PEarthCraft.getInstance().getDB();
		Connection c = db.getConnection();
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery(String.format("SELECT `structureID` FROM chunks WHERE `x`>=%d AND `x`<=%d AND `z`>=%d AND `z`<=%d; ", ch.getX(),(ch.getX()+getSizeChX()-1),ch.getZ(),(ch.getZ()+getSizeChZ()-1)));
		if(r.next()) {return false;}
		s.executeUpdate(String.format("INSERT INTO structures (`x`,`y`,`z`,`name`,`direction`,`owner`) VALUES (%d,%d,%d,'%s','%s','%s');",x,y,z,name,direction,Owner));
		r = s.executeQuery(String.format("SELECT `id` FROM structures WHERE `x`=%d AND `z`=%d;",x,z ) );
		if(!r.next()) {
			System.out.println("Нет такой структуры, хоть я ее только что создал");
			return false;
		}
		int id = r.getInt("id");
		StructureObj obj = createObj(id,x,y,z,this,Owner);
		StructureObj.registry.put(id, obj);
		World w = PEarthCraft.getInstance().getWorld();
		String cord;
		for(int xi=0;xi< getSizeChX();xi++) {
			for(int zi=0;zi< getSizeChZ();zi++) {
				PieceStructureObj pso = new PieceStructureObj(id,xi,zi,ch.getX() + xi,y,ch.getZ() + zi,  struct[xi][zi], obj);
				cord = (ch.getX() + xi) + ":" + (ch.getZ() + zi);
				PieceStructureObj.registry.put(cord, pso);
				s.executeUpdate(String.format("INSERT INTO chunks (`cords`,`x`,`y`,`z`,`offsetx`,`offsetz`,`structureID`) VALUES ('%s',%d,%d,%d,%d,%d,%d)" + 
						"  ON DUPLICATE KEY UPDATE `structureID`=%d,`y`=%d,`offsetx`=%d,`offsetz`=%d;",cord,ch.getX()+xi,y,ch.getZ()+zi,xi,zi,id,id,y,xi,zi));// вставить или обновить таблицу chunks 
			}
		}
		s.close();
		c.close();
		obj.build();
		DynmapManager.getInstance().paint(id +":" + name, "Owner: " + Owner, 0xffffff, w.getBlockAt(x, y, z), w.getBlockAt(x+sizex, y, z+sizez));
		return true;
	}
	public int getPrimeHeight(int chx,int chz) {
		int bx=chx*16,bz=chz*16,sum=0;
		World w = PEarthCraft.getInstance().getWorld();
		for(int x =0; x<sizechx*16;x++) {
			for (int z = 0; z < sizechz*16; z++) {
				sum+=w.getHighestBlockYAt(bx+x, bz+z);
			}
		}
		return sum/((sizechx*16)*(sizechz*16));
	}
	public static boolean chechFoundation() {
		return true;
	}
	protected abstract StructureObj createObj(int id,int x,int y,int z,PatternStructure pattern,String Owner);
	public abstract void executeCommand(Block b,String cmd);
}
