package org.MetaCringer.Events;

import org.MetaCringer.Structures.PieceStructureObj;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class EHPrivateblock implements Listener{
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		PieceStructureObj obj;
		Chunk ch=e.getBlock().getChunk();
		Block b = e.getBlock();
		
		e.getPlayer().sendMessage(ChatColor.BOLD + "" +  (( (obj =PieceStructureObj.registry.get(ch.getX()+":"+ch.getZ())) != null)? obj.getStructureID() :"null"));
		if( ((obj = PieceStructureObj.registry.get(ch.getX()+":"+ch.getZ()) ) !=null) && (!obj.canBreak(b.getX(), b.getY(), b.getZ())) ) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		PieceStructureObj obj;
		Chunk ch=e.getBlockPlaced().getChunk();
		Block b = e.getBlockPlaced();
		e.getPlayer().sendMessage(ChatColor.BOLD + "" +  (( (obj =PieceStructureObj.registry.get(ch.getX()+":"+ch.getZ())) != null)? obj.getStructureID() :"null"));
		if( ((obj = PieceStructureObj.registry.get(ch.getX()+":"+ch.getZ()) ) !=null) && (!obj.canBreak(b.getX(), b.getY(), b.getZ())) ) {
			e.setCancelled(true);
		}
	}
}
