package org.MetaCringer.Structures.patterns;

import org.MetaCringer.Structures.objects.DefaultStructureObj;
import org.MetaCringer.Structures.objects.StructureObj;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class PatDefault extends PatternStructure {

	protected PatDefault(String name, int sizex, int sizey, int sizez) {
		super(name, sizex, sizey, sizez);
		
	}

	@Override
	public void executeCommand(Block b, String cmd) {
		//b.setTypeId(68);
		Sign s =(Sign) b.getState();
		
		String[] args = cmd.split(",");
		for(int i = 0;i<args.length;i++) {
			s.setLine(i, args[i]);
		}
		s.update();
		
	}

	@Override
	public StructureObj createObj(int id, int x, int y, int z, PatternStructure pattern, String Owner) {//Доделать TODO
		return new DefaultStructureObj(id, x, y, z, pattern, Owner);
	}


}
