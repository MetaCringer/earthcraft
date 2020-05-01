package Commands;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.util.Color;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CRegenChunk implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only for Player");
			return false;
		}
		Player p = (Player)sender;
		if(!p.isOp()) {
			sender.sendMessage("Only for Ops");
			return false;
		}
		Chunk ch = p.getLocation().getChunk();
		p.getWorld().regenerateChunk(ch.getX(), ch.getZ());
		//PEarthCraft.getInstance().getMapManager().paint(ch.getX() + ":" +ch.getZ(), new Color((byte)0,(byte)0 ,(byte)255 ), p.getLocation().getChunk());
		return true;
		
	}
}
