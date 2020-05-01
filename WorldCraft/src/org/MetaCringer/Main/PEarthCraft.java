package org.MetaCringer.Main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;

import org.MetaCringer.Events.EHCanceler;
import org.MetaCringer.Events.EHChunkListener;
import org.MetaCringer.Events.EHFoodSpawner;
import org.MetaCringer.Events.EHJoinPlayer;
import org.MetaCringer.Events.EHPrivateblock;
import org.MetaCringer.World.MapManager;
import org.MetaCringer.World.WorldGenerator;
import org.MetaCringer.dynmap.DynmapManager;
import org.MetaCringer.mobs.BushEntity;
import org.MetaCringer.mobs.CustomEntityRegistry;
import org.MetaCringer.mobs.CustomZombie;
import org.MetaCringer.test.mobs;
import org.MetaCringer.util.Color;
import org.MetaCringer.util.sql.SQLDatabase;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandSender.Spigot;
import org.bukkit.entity.Entity;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import com.google.common.io.Files;

import Commands.CBuild;
import Commands.CCheck;
import Commands.CDestroy;
import Commands.CDynMap;
import Commands.CRegenChunk;

public class PEarthCraft extends JavaPlugin {
	private static PEarthCraft plugin;

	public static void main(String[] args) throws URISyntaxException {
		
		//System.out.println(new File("assets/templates/spawn.def").exists());
		JarFile jar;
		try {
			jar = new JarFile(new File(test.class.getProtectionDomain().getCodeSource().getLocation()
				    .toURI()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Enumeration<JarEntry>entries =jar.entries();
		String res;
		while(entries.hasMoreElements()) {
			System.out.println(entries.nextElement().getName());
		}
		//PEarthCraft p = new PEarthCraft();
		//File d = new File("assets/");
		//saveDir("assets/",new File("test/"), false);
	}
	public class test {
		
	}
	public static final String WORLDNAME = "earthcraft";
	private World world;
	MapManager mapManager;
	private int waterlevel = 30;
	private SQLDatabase db;
	public SQLDatabase getDB() {
		return db;
	}
	public World getWorld() {
		return world;
	}
	public static PEarthCraft getInstance() {
		return plugin;
		
	}
	@Override
	public void onLoad() {
		//saveResource("assets/map.png", false);
		//saveResource("assets/Heights.png", false);
		//saveResource("assets/oresRCI.png", false);
		//saveResource("assets/oresPGD.png", false);
		//saveResource("assets/BiomesSettings.txt", false);
		//saveResource("assets/Patterns.txt", false);
		saveResource("config.yml",false);
		saveDir("assets/", false);
		
		
	}
	
	public void saveDir(String dir, /*File dest,*/boolean replace) {
		JarFile jar;
		try {
			jar = new JarFile(this.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Enumeration<JarEntry>entries =jar.entries();
		String res;
		while(entries.hasMoreElements()) {
			res = entries.nextElement().getName();
			if(res.startsWith(dir)) {
				saveResource(res, replace);
			}
			
			
		}/*
		FileUtils.copyToFile(this.getResource(""), arg1);
		this.getResource(filename)
		File d = new File(dir);
		if(!d.exists()) {
			return;
		}
		if(d.isFile()) {
			try {
				
				File file = new File(this.getDataFolder(),d.getPath() );
				
				//System.out.println(file.getPath() + "  " + file.isFile());
				//file.getParentFile().mkdirs();
				if(!file.exists() || replace) {
					Files.copy(d, file);
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		for (File f : d.listFiles()) {
			saveDir(f.getPath(), replace);
		}*/
	}
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		plugin = this;
		try {
			mapManager = new MapManager(new File(getDataFolder(), "assets/map.png"),
					new File(getDataFolder(), "assets/BiomesSettings.txt"),
					new File(getDataFolder(), "assets/Heights.png"),
					new File(getDataFolder(), "assets/Patterns.txt"),
					new File(getDataFolder(), "assets/oresRCI.png"),
					new File(getDataFolder(), "assets/oresPGD.png"));
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		try {
			db = new SQLDatabase();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.out.println("/n/n Cannot init Database. EarthCraft is disabled /n/n");
			e.printStackTrace();
			onDisable();
		}
		catch (SQLException e) {
			System.out.println("/n/n Create connection failed /n/n");
			e.printStackTrace();
			onDisable();
		}
		
		WorldCreator wc = new WorldCreator(WORLDNAME).environment(Environment.NORMAL)
				.generateStructures(false).generator(getDefaultWorldGenerator(WORLDNAME, 0+""))
				.seed(System.currentTimeMillis());
		world = Bukkit.createWorld(wc);
		world.setSpawnFlags(true, true);
		world.setSpawnLocation(0, 65, 0);
		world.setDifficulty(Difficulty.HARD);
		world.setAutoSave(true);
		world.setPVP(true);
		Bukkit.getPluginManager().registerEvents(new EHJoinPlayer(this), this);
		Bukkit.getPluginManager().registerEvents(new EHFoodSpawner(mapManager), this);
		Bukkit.getPluginManager().registerEvents(new EHPrivateblock(), this);
		Bukkit.getPluginManager().registerEvents(new EHChunkListener(), this);
		Bukkit.getPluginManager().registerEvents(new EHCanceler(), this);
		//Bukkit.getPluginManager().registerEvents(new mobs(),this);
		getCommand("regen").setExecutor(new CRegenChunk());
		getCommand("color").setExecutor(new CDynMap());
		getCommand("build").setExecutor(new CBuild());
		getCommand("destroy").setExecutor(new CDestroy());
		getCommand("check").setExecutor(new CCheck());
		CustomEntityRegistry.registerCustomEntity(55, "Bush", BushEntity.class);
		//CustomEntityRegistry.registerCustomEntity(54, "Zombie", CustomZombie.class);
		/*
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		String command = "/mw create worldcraft Plugin:WorldCraft " + System.currentTimeMillis();
		Bukkit.dispatchCommand(console, command);
		Bukkit.dispatchCommand(console, "/mw load worldcraft");
		*/
		DynmapAPI dyn = (DynmapAPI) getServer().getPluginManager().getPlugin("dynmap");
		new DynmapManager(this, dyn);
		//mapManager.createProvinceZone("bla123", new Color((byte)0,(byte)0 ,(byte)255 ), world.getChunkAt(0, 0));
		
		/*getDataManager().getWorldManager().makeWorld("earth1", new net.minecraft.server.v1_12_R1.WorldGenerator() {
			
			@Override
			public boolean generate(net.minecraft.server.v1_12_R1.World arg0, Random arg1, BlockPosition arg2) {
				// TODO Auto-generated method stub
				return false;
			}
		}, (long)1, "");*/
		
		/*getServer().getWorlds().get(0).getPopulators().add(new BlockPopulator() {
			
			@Override
			public void populate(World arg0, Random arg1, Chunk arg2) {
				// TODO Auto-generated method stub
				
			}
		});*/
		for (Chunk ch : world.getLoadedChunks()) {
			ch.unload();
		}
		
		
	}
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
		for (Chunk ch : world.getLoadedChunks()) {
			for (Entity ent : ch.getEntities()) {
				ent.remove();
			}
		}
	}
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new WorldGenerator(this, mapManager,waterlevel);
	}
	

	
	
}
