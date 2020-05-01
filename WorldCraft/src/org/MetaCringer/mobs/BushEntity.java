package org.MetaCringer.mobs;

import org.MetaCringer.Main.PEarthCraft;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.metadata.FixedMetadataValue;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.EntitySlime;
import net.minecraft.server.v1_12_R1.World;

public class BushEntity extends EntitySlime {
	@SuppressWarnings("deprecation")
	public BushEntity(World world,double x,double y, double z) {
		super(world);
		Slime cslime = (Slime)this.getBukkitEntity();
		this.setCustomName(ChatColor.DARK_GREEN +"Bush of berries");
		this.setCustomNameVisible(true);
		cslime.setMaxHealth(1);
		this.setHealth(1);
		
		cslime.setCollidable(false);
		
		cslime.setAI(false);
		//cslime.setGravity(false);
		cslime.setSize(1);
		this.getWorld().addEntity(this);
		this.setPosition(x, y, z);
		((Entity)cslime).setMetadata("bush", new FixedMetadataValue(PEarthCraft.getInstance(), ""));
		((Entity)cslime).setMetadata("custom", new FixedMetadataValue(PEarthCraft.getInstance(), ""));
		//this.targetSelector;
		//this.goalSelector.
	}
	

}
