package Commands;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.dynmap.DynmapManager;
import org.MetaCringer.util.Color;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CDynMap implements CommandExecutor {
	PEarthCraft p;
	public CDynMap() {
		p = PEarthCraft.getInstance();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			DynmapManager.getInstance().resetMarkers();
			return true;
		}
		return false;
	}
	
	
	
}
