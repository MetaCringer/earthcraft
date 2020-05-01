package Commands;

import java.io.IOException;
import java.sql.SQLException;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.Structures.PieceStructureObj;
import org.MetaCringer.Structures.objects.StructureObj;
import org.MetaCringer.Structures.patterns.PatternStructure;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CBuild implements CommandExecutor,Listener {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {return false;}
		if(sender instanceof Player) {
			Player p = (Player) sender;
			Location l = p.getLocation();
			String direction;
			int offsetx=0,offsetz=0;
			float yaw = (l.getYaw()+225)%360;
			if(yaw < 90) {
				direction="_north";
				offsetz=-1;
			}else if(yaw < 180) {
				direction="_east";
				offsetx=1;
			}else if(yaw < 270) {
				direction="_south";
				offsetz=1;
			}else {
				direction="_west";
				offsetx=-1;
			}
			PatternStructure struct;
			if((struct = PatternStructure.getStructure(args[0]+direction))==null) {
				try {
					if(!PatternStructure.loadStructure(args[0], direction, "default")) {
						p.sendMessage("Не находит файл или не по сетке структура");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if((struct = PatternStructure.getStructure(args[0]+direction))==null) {
					sender.sendMessage("Не удаеться загрузить шаблон структуры");
					return true;
				}
				
			}
			if(offsetx<0) {
				offsetx *= struct.getSizeChX();
			}
			if(offsetz<0) {
				offsetz *= struct.getSizeChZ();
			}
			Chunk ch = p.getLocation().getChunk();
			int y =struct.getPrimeHeight(ch.getX()+offsetx, ch.getZ()+offsetz);
			try {
				ch = PEarthCraft.getInstance().getWorld().getChunkAt(ch.getX()+offsetx, ch.getZ()+offsetz);
				Block b = ch.getBlock(0, y, 0);
				if(!struct.createStructureObjWithDB(b.getX(), y, b.getZ(), p.getName())) {
					p.sendMessage("Место занято другой структурой");
				}
			} catch (SQLException e) {
				p.sendMessage(ChatColor.RED + e.getMessage());
				e.printStackTrace();
			}//Спроецируй так чтобы появлялось здание перед тобой
			return true;
			
		}
		return false;
	}
	

}
