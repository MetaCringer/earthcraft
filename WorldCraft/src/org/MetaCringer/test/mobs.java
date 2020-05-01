package org.MetaCringer.test;



import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.mobs.BushEntity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class mobs implements Listener {
	@EventHandler
	public void onSpawn(PlayerInteractEvent e) {
		if(Action.LEFT_CLICK_BLOCK.equals(e.getAction())) {
			Block b = e.getClickedBlock();
			Location l;
			BushEntity ent = new BushEntity(((CraftWorld) b.getWorld()).getHandle(),(l = b.getLocation()).getX(), l.getY()+1, l.getZ());
			

			((Entity)ent.getBukkitEntity()).setMetadata("bush", new FixedMetadataValue(PEarthCraft.getInstance(), "keyword123"));
			e.setCancelled(true);
			
			
		}
		
	}
	@EventHandler
	public void onCheck(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked().hasMetadata("bush")) {
			e.getPlayer().sendMessage(ChatColor.GREEN + "bush = " + e.getRightClicked().getMetadata("bush").get(0).asString());
		}
	}
	
}
