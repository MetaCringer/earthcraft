package org.MetaCringer.Events;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.MetaCringer.Main.PEarthCraft;
import org.MetaCringer.Structures.PieceStructureObj;
import org.MetaCringer.Structures.objects.StructureObj;
import org.MetaCringer.Structures.patterns.PatternStructure;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class EHChunkListener implements Listener {//TODO Не работат
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLoad(ChunkLoadEvent e) {
		Chunk ch = e.getChunk();
		PieceStructureObj pso = PieceStructureObj.getChunk(ch.getX(), ch.getX());
		if(pso != null) {
			pso.executeCommands();
			return;
		}//В этом чанке есть структура
		try {
			Connection c = PEarthCraft.getInstance().getDB().getConnection();
			Statement s = c.createStatement();
			ResultSet r = s.executeQuery("SELECT * FROM structures WHERE `id` = ANY(SELECT `structureID` FROM chunks WHERE `cords` = '"+ ch.getX() +":"+ ch.getZ() +"');");
			if(!r.next()) {
				s.close();
				c.close();
				return;
			}//Удостоверились что ее нет в БД
			System.out.println("Нашелся чанк структуры по БД " + ch.getX() + ":" +ch.getZ());
			int id = r.getInt("id");
			String name,direction;
			PatternStructure ps =PatternStructure.getStructure((name = r.getString("name"))+(direction =r.getString("direction")));
			if(ps == null) {//Проверяем подгружен ли паттерн
				try {
					if(!PatternStructure.loadStructure(name, direction, "default")) {//Подгружаем
						System.out.println("Не удаеться загрузить " + name+direction);//TODO добавить везде столбец theme
						s.close();
						c.close();
						return;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					s.close();
					c.close();
					return;
				} 
				ps =PatternStructure.getStructure(name+direction);
			}//Паттерн готов
			
			
			Block b = ch.getBlock(0, r.getInt("y"), 0);
			ps.createStructureObj(id,b.getX(), b.getY(), b.getZ(), r.getString("owner"));//Регистрируем в хешмапах структуру и ее чанки
			pso = PieceStructureObj.getChunk(ch.getX(), ch.getZ());
			System.out.println("Пытаемся взять ch:"+ch.getX() + ":" + ch.getZ());
			if(pso!=null) {
				pso.executeCommands();//Исполнение скриптов
			}else{
				System.out.println("Ничего не помогло подгрузить PieceStructureObj");
			}
			s.close();
			c.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
//SELECT `structureID` FROM chunks WHERE `structureID` = ANY (SELECT `structureID` FROM chunks WHERE `cords` = "+ ch.getX() +":"+ ch.getZ() +" );
//SELECT * FROM structures WHERE `id` = ANY(SELECT `structureID` FROM chunks WHERE `cords` = "+ ch.getX() +":"+ ch.getZ() +");