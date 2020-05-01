package org.MetaCringer.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EHCanceler implements Listener {
	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
		if(!e.getEntity().hasMetadata("custom")) {
			
			e.setCancelled(false);
		}
	}
}
