package org.MetaCringer.Events;

import org.MetaCringer.Main.PEarthCraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EHJoinPlayer implements Listener {
	PEarthCraft plugin;
	public EHJoinPlayer(PEarthCraft p) {
		plugin = p;
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		plugin.getLogger().info(p.getUniqueId().toString());
		if(!p.getLocation().getWorld().getName().equals(plugin.WORLDNAME)) {
			
			p.teleport(Bukkit.getWorld(plugin.WORLDNAME).getSpawnLocation());
			
		}
		
	}
}
