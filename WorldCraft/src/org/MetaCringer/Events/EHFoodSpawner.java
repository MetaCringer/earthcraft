package org.MetaCringer.Events;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.World.MapManager;
import org.MetaCringer.mobs.BushEntity;
import org.MetaCringer.mobs.CustomZombie;
import org.MetaCringer.util.sql.SQLDatabase;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import net.minecraft.server.v1_12_R1.WorldServer;



public class EHFoodSpawner implements Listener {
	Random random;
	SQLDatabase db;
	int maxfood=3;
	MapManager mm;
	private Connection c;
	public EHFoodSpawner(MapManager mm) {
		db = PEarthCraft.getInstance().getDB();
		this.mm = mm;
		random = new Random();
		
	}
	public Statement getStatement() throws SQLException {
		if(c == null || c.isClosed()) {
			c = db.getConnection();
		}
		return c.createStatement();
		
	}
	@EventHandler
	public void onLoad(ChunkLoadEvent e) {
		Chunk ch = e.getChunk();
		Biome b = mm.GetBiome(ch.getX(), ch.getZ());
		if (!PEarthCraft.getInstance().WORLDNAME.equals(ch.getWorld().getName()) || Biome.OCEAN.equals(b) || Biome.DESERT.equals(b)) {
			return;
		}
		
		
		//Connection c;
		Statement s;
		ResultSet r;
		int amount;
		try {
			
			s = getStatement();
			r = s.executeQuery("SELECT `id` , `amount` FROM foods WHERE `id` = '" + ch.getX() + ":" + ch.getZ() + "';");
			
			if(r.next()) {
				amount = r.getInt("amount");
			}
			else {
				//s.executeUpdate("INSERT INTO foods (`id`,`amount`) VALUES('" + ch.getX() + ":" + ch.getZ() + "'," + maxfood + ") ON DUPLICATE KEY UPDATE `amount` = "+maxfood+";");
				amount= maxfood;
			}
			
			if(amount < 1) {return;}
			World w = ch.getWorld();
			
			int x = random.nextInt(16),z = random.nextInt(16);
			Location sp = ch.getBlock(x, 0, z).getLocation();
			sp.setY(w.getHighestBlockYAt(sp.getBlockX(), sp.getBlockZ()));
			WorldServer nmsw = ((CraftWorld) w).getHandle(); 
			
			BushEntity bush = new BushEntity(nmsw,sp.getX()+0.5, sp.getY(), sp.getZ()+0.5);
			
			//((Entity)bush.getBukkitEntity()).setMetadata("bush", new FixedMetadataValue(PEarthCraft.getInstance(), "234"));
			//System.out.println("spawned bush at " + sp.getX()+":"+ sp.getY()+":"+ sp.getZ() + " amount = " + amount);
			//nmsw.getTracker().untrackEntity(bush);	
			s.close();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	@EventHandler
	public void onUnload(ChunkUnloadEvent e) {
		for (Entity ent : e.getChunk().getEntities()) {
			ent.remove();
		}
	}
	@EventHandler
	public void onUnload(WorldUnloadEvent e) {
		for (Chunk ch : e.getWorld().getLoadedChunks()) {
			for (Entity ent : ch.getEntities()) {
				ent.remove();
			}
		}
	}
	
	
	@EventHandler
	public void onFish(PlayerFishEvent e) {
		
	}
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		
		if(e.getEntity().hasMetadata("bush")) {//не выполняеться это условие, в чём дело???TODO
			//e.getEntity().remove();
			e.setDroppedExp(0);
			e.getDrops().clear();
			//e.getDrops().add(new ItemStack(Material.APPLE));
			e.getEntity().getLocation().getWorld().dropItem(e.getEntity().getLocation(), new ItemStack(Material.APPLE));
			Chunk ch = e.getEntity().getLocation().getChunk();
			Statement s;
			try {
				s = getStatement();
				s.executeUpdate("INSERT INTO foods (`id`,`amount`) VALUES('" + ch.getX() + ":" + ch.getZ() + "'," + (maxfood-1) + ") ON DUPLICATE KEY UPDATE `amount` = `amount`-1;");
				s.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	@EventHandler
	public void onCheck(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked().hasMetadata("custom")) {
			e.getPlayer().sendMessage(ChatColor.GREEN + "bush = " + e.getRightClicked().getMetadata("bush").get(0).asString());
		}else {
			e.getPlayer().sendMessage(ChatColor.RED + "bush = null");
		}
	}
}
