package org.MetaCringer.World;

import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.MetaCringer.util.Color;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class MapManager {
	
	public static void main(String[] args) throws IOException { // //TODO свой редактор карт
	    // ƒовольно простой можно мен€ть отображаемые файлы
		// можно выбрать цвет r g b который будет редактироватьс€ в каждом пикселе мышкой
		// колесиком мыши выбирать значение, левой кнопкой установить значение в пикселе
		// √ениально
		
		/*
		File h=  new File("assets/Heights.png"),p = new File("assets/Patterns.txt");
		h.mkdirs();
		h.createNewFile();
		p.mkdirs();
		p.createNewFile();
		MapManager map = new MapManager(new File("assets/map.png"),new File("assets/BiomesSettings.txt"),h, p);
		
		JFrame f =new JFrame(" арта генерации");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new JLabel(new ImageIcon(biomesgrid)));
		f.setSize(biomesgrid.getWidth(), biomesgrid.getHeight());
		f.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg) {
				// TODO Auto-generated method stub
				Color c = new Color(biomesgrid.getRGB(arg.getX(), arg.getY()));
				f.setTitle(arg.getX()-(biomesgrid.getWidth()/2)+" " + (arg.getY()-(biomesgrid.getHeight()/2)) + " Biome: " + 
				GetBiome(arg.getX()-(biomesgrid.getWidth()/2),arg.getY()-(biomesgrid.getHeight()/2))  + 
				" RGB: " + Byte.toUnsignedInt(c.getR()) + " " + Byte.toUnsignedInt(c.getG()) + " " 
				+ Byte.toUnsignedInt(c.getB()) +" ");
				
			}
		});
		f.setVisible(true);
		*/
		
		
	}
	
	
	
	private BufferedImage biomesgrid;
	private BufferedImage MapHeights;
	private BufferedImage oresRCI;
	private BufferedImage oresPGD;
	private Map<Color, Biome> CMB=new HashMap<Color, Biome>();
	private File patterns;
	public MapManager(File BiomeMap,File cmb,File MapHeights,File patterns,File oresRCI, File oresPGD) throws IOException {
		biomesgrid = ImageIO.read(BiomeMap);
		this.MapHeights = ImageIO.read(MapHeights);
		this.oresRCI = ImageIO.read(oresRCI);
		this.oresPGD = ImageIO.read(oresPGD);
		this.patterns = patterns;
		BufferedReader in = new BufferedReader(new InputStreamReader( new FileInputStream(cmb)));
		
		String[] args;
		while(in.ready()) {
			args = in.readLine().split(":",4);
			CMB.put(new Color( (byte)Integer.parseInt(args[0]) ,
					(byte)Integer.parseInt(args[1]), (byte)Integer.parseInt(args[2])), 
					Biome.valueOf(args[3]));
		}
		
	}
	
	public Map<Material,Double> getChanceOreSpawn(int chx,int chz) {
		Map<Material, Double> result = new HashMap<Material,Double>();
		int RCI = oresRCI.getRGB((oresRCI.getWidth()/2) +chx, (oresRCI.getHeight()/2)+chz);
		int PGD = oresPGD.getRGB((oresPGD.getWidth()/2) +chx, (oresPGD.getHeight()/2)+chz);
		result.put(Material.REDSTONE_ORE, ((double)((RCI >> 16) & 0xff))/255 );
		result.put(Material.COAL_ORE, ((double)((RCI >> 8) & 0xff))/255 );
		result.put(Material.IRON_ORE, ((double)(RCI & 0xff))/255 );
		result.put(Material.LAPIS_ORE, ((double)((PGD >> 16) & 0xff))/255 );
		result.put(Material.GOLD_ORE, ((double)((PGD >> 8) & 0xff))/255 );
		result.put(Material.DIAMOND_ORE, ((double)(PGD & 0xff))/255 );
		return result;
	}
	
	
	public Biome GetBiome(int chx,int chz) {
		int sizex=0,sizez=0;
		if((biomesgrid == null)||((sizex = biomesgrid.getWidth()) < (chx/2)) || ((sizez = biomesgrid.getHeight()) < (chz/2) )) {
			return Biome.SKY;
		}
		Color c = new Color(biomesgrid.getRGB((sizex/2) +chx, (sizez/2)+chz));
		
		for (Entry<Color, Biome> e : CMB.entrySet()) {
			if(e.getKey().compare(c)) {return e.getValue();}
		}
		return Biome.SKY;
	}
	public int getHeight(int chx,int chz) {
		int sizex,sizez;
		if( (MapHeights == null) || ((sizex = MapHeights.getWidth()) < (chx/2)) || ((sizez = MapHeights.getHeight()) < (chz/2) )) {
			return 0;
		}
		return ((MapHeights.getRGB((sizex/2) +chx, (sizez/2)+chz) >> 16) & 0xff);
	}
	public Map<Biome,List<Short>> GetPatterns() throws NumberFormatException, IOException{//TODO ƒоделать
		BufferedReader in = new BufferedReader(new InputStreamReader( new FileInputStream(patterns)));
		Map<Biome,List<Short>> result = new HashMap<Biome,List<Short>>();
		Biome key;
		List<Short> value;
		
		String[] args;
		while(in.ready()) {
			args = in.readLine().split(":",2);
			key = Biome.valueOf(args[0]);
			args = args[1].split(" ");
			value = new ArrayList<Short>();
			for (String block : args) {
				value.add(Short.parseShort(block));
			}
			result.put(key, value);
		}
		return result;
	}
}
