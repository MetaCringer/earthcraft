package Commands;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.Structures.PieceStructureObj;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CCheck implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players");
			return true;
		}
		Player p = (Player)sender;
		Chunk ch = p.getLocation().getChunk();
		int regid,bdid;
		PieceStructureObj obj;
		if ((obj = PieceStructureObj.registry.get(ch.getX() + ":" + ch.getZ())) != null) {
			p.sendMessage(ChatColor.GREEN +"structure id: " + obj.getStructureID());
		}else{
			p.sendMessage(ChatColor.RED +"structure id: null");
		}
		try {
			Connection c = PEarthCraft.getInstance().getDB().getConnection();
			Statement s = c.createStatement();
			ResultSet r = s.executeQuery("SELECT `structureID` FROM chunks WHERE `cords` = '"+(ch.getX() + ":" + ch.getZ())+"';");
			if(r.next()) {
				p.sendMessage(ChatColor.GREEN +"DB structure id: " + r.getInt("structureID"));
			}else {
				p.sendMessage(ChatColor.RED +"DB structure id: null");
			}
			s.close();
			c.close();
		} catch (SQLException e) {
			p.sendMessage(ChatColor.RED + e.getMessage());
			e.printStackTrace();
		}
		return true;
		
	}

}
