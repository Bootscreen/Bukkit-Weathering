package me.bootscreen.weathering;

import java.io.File;

import org.bukkit.util.config.*;


/**
* Weathering for CraftBukkit
*
* handels all functions about the config
* 
* @author Bootscreen
*
*/


public class FileManager {
	
	private static String ordner = "plugins/Weathering";
	private static File configFile = new File(ordner + File.separator + "config.yml");
	@SuppressWarnings("deprecation")
	private static Configuration config;

	@SuppressWarnings("deprecation")
	private Configuration loadConfig()
	{
		try{
			Configuration config = new Configuration(configFile);
			config.load();
			return config;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void createConfig()
	{
		new File(ordner).mkdir();
		
		if(!configFile.exists())
		{
			try 
			{
				configFile.createNewFile();
				
				config = loadConfig();
				//config.setHeader(header)
				config.setProperty("CobbleStone.Chance", 40);
				config.setProperty("CobbleStone.nearby_Chance", 30);
				config.setProperty("Stonebricks.Chance", 30);
				config.setProperty("Stonebricks.mossy_Chance", 50);
				config.setProperty("Stonebricks.nearby_mossyChance", 30);
				config.setProperty("Stonebricks.nearby_brickedChance", 30);

				config.save();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		config = loadConfig();
	}
	
	public String readString(String key)
	{
		String value = config.getString(key,"");
		return value;
	}
}
