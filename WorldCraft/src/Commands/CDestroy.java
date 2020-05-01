package Commands;

import java.sql.SQLException;

import org.MetaCringer.Structures.objects.StructureObj;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CDestroy implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(args.length == 0) {
			return false;
		}
		int id = Integer.parseInt(args[0]);
		StructureObj obj = StructureObj.registry.get(new Integer(id));
		if(obj == null) {
			sender.sendMessage("Такой структуры нет...");
			return true;
		}
		try {
			obj.deleteStructure();
		} catch (SQLException e) {
			if(sender instanceof Player) {
				sender.sendMessage(ChatColor.RED + e.getMessage());
			}
			e.printStackTrace();
		}
		return true;
	}

}
