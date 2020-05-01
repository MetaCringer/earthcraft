package org.MetaCringer.util.sql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.MetaCringer.Main.PEarthCraft;
import org.bukkit.configuration.file.FileConfiguration;

public class SQLDatabase {
	Connection c;
	private boolean mysqlEnable;
	String url,user,password,host,dbname;
	int port;
	public SQLDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException  {
		FileConfiguration fc = PEarthCraft.getInstance().getConfig();
		/*
		if(mysqlEnable = fc.getBoolean("mySQL.externaldatabase")  ) {
			user= fc.getString("mySQL.user");
			password = fc.getString("mySQL.password");
			host = fc.getString("mySQL.host");
			port = fc.getInt("mySQL.port");
			dbname = fc.getString("mySQL.dbname");
			url = "jdbc:mysql//" + host + ":" + port + "/" + dbname;
			
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				getConnection().close();
			} catch (Exception e) {
				
				url = "jdbc:sqlite:" + PEarthCraft.getInstance().getDataFolder() + File.separator + "database.db";
				user = null;
				Class.forName("org.sqlite.JDBC").newInstance();
				getConnection().close();
			} 
		}else {
			url = "jdbc:sqlite:" + PEarthCraft.getInstance().getDataFolder() + File.separator + "database.db";
			Class.forName("org.sqlite.JDBC").newInstance();
			getConnection().close();
		}
		createWorldTables();
	*/
		user= fc.getString("mySQL.user");
		password = fc.getString("mySQL.password");
		host = fc.getString("mySQL.host");
		port = fc.getInt("mySQL.port");
		dbname = fc.getString("mySQL.dbname");
		url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?useSSL=false";
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		getConnection().close();
		
		createWorldTables();
	}
	
	public Connection getConnection() throws SQLException {
		if(user != null) {
			return DriverManager.getConnection(url, user, password);
		}else {
			return DriverManager.getConnection(url);
		}
	}
	
	
	public void createWorldTables() throws SQLException{
		Connection c = getConnection();
		Statement s = c.createStatement();
		s.executeUpdate("CREATE TABLE IF NOT EXISTS foods (`id` VARCHAR(24) PRIMARY KEY, `amount` INT);");
		s.executeUpdate("CREATE TABLE IF NOT EXISTS chunks (`cords` varchar(16) NOT NULL,`x` int NOT NULL,`y` int NOT NULL,`z` int NOT NULL,`offsetx` int NOT NULL,`offsetz` int NOT NULL, `structureID` int, PRIMARY KEY (`cords`));");
		// x i y - кординаты чанка а н высота
		s.executeUpdate("CREATE TABLE IF NOT EXISTS structures (`id` int NOT NULL AUTO_INCREMENT,`x` int NOT NULL,`y` int NOT NULL,`z` int NOT NULL,"
				+ "`name` varchar(24) NOT NULL,`direction` varchar(6) NOT NULL,`owner` varchar(24), PRIMARY KEY (`id`));");
		
		s.close();
		c.close();
	}
	
}
