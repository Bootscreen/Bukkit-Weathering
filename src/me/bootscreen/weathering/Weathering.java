package me.bootscreen.weathering;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
* Weathering for CraftBukkit
* 
* @author Bootscreen
*
*/

public class Weathering extends JavaPlugin{
	public final Logger log = Logger.getLogger("Minecraft");
	FileConfiguration config = null;
	PluginDescriptionFile plugdisc;

	int cs_chance, cs_nearbychance, sb_chance, sb_mossychance, sb_nearbymossychance, sb_nearbybrickedchance;

	@Override
	public void onDisable() 
	{
		log.info("[" + plugdisc.getName() + "] Version " + plugdisc.getVersion() + " disabled.");		
	}
	

	@Override
	public void onEnable() 
	{
		config = this.getConfig();
		loadConfig();
		
		plugdisc = this.getDescription();
		
		log.info("[" + plugdisc.getName() + "] Version " + plugdisc.getVersion() + " enabled.");

		try
		{
			cs_chance = config.getInt("CobbleStone.Chance");
			cs_nearbychance = config.getInt("CobbleStone.nearby_Chance");
			sb_chance = config.getInt("Stonebricks.Chance");
			sb_mossychance = config.getInt("Stonebricks.mossy_Chance");
			sb_nearbymossychance = config.getInt("Stonebricks.nearby_mossyChance");
			sb_nearbybrickedchance = config.getInt("Stonebricks.nearby_brickedChance");
		}
		catch(NumberFormatException e)
		{
			cs_chance = 40;
			cs_nearbychance = 30;
			sb_chance = 30;
			sb_mossychance = 50;
			sb_nearbymossychance = 30;
			sb_nearbybrickedchance = 30;
			log.info("[" + plugdisc.getName() + "] config load failed because the values aren't numeric, load internal standard values.");
		}
		catch(Exception e)
		{
			cs_chance = 40;
			cs_nearbychance = 30;
			sb_chance = 30;
			sb_mossychance = 50;
			sb_nearbymossychance = 30;
			sb_nearbybrickedchance = 30;
			log.info("[" + plugdisc.getName() + "] config load failed, load internal standard values.");
			e.printStackTrace();
		}
		cs_chance = 100 - cs_chance;
		cs_nearbychance = 100 - cs_nearbychance;
		sb_chance = 100 - sb_chance;
		sb_mossychance = 100 - sb_mossychance;
		sb_nearbymossychance = 100 - sb_nearbymossychance;
		sb_nearbybrickedchance = 100 - sb_nearbybrickedchance;
	}
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean succeed = false;
		
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("weathering") || cmd.getName().equalsIgnoreCase("wtg"))
			{
				if(args.length == 1)
				{
					if(player.hasPermission("weathering")) 
					{
						int radius = 0, posx = 0, posy = 0, posz = 0;
						World world;
	
						player.sendMessage(ChatColor.GREEN + "[" + plugdisc.getName() + "] weathering startet.");
						
						try
						{
			            	Random generator = new Random();
			            	Block block;
							radius = Integer.parseInt(args[0]);
							int rand = 0, rand2 = 0, rand3 = 0, i = 0;
							String[] coords = new String[2*radius*2*radius*2*radius];
							Arrays.fill(coords,"");
							posx = player.getLocation().getBlockX();
							posy = player.getLocation().getBlockY();
							posz = player.getLocation().getBlockZ();
							world = player.getWorld();
							
							for (int dx = (posx-radius); dx < (posx+radius); dx++)
							{
							    for (int dy = (posy-radius); dy < (posy+radius); dy++) 
							    {
							        for (int dz = (posz-radius); dz < (posz+radius); dz++) 
							        {
							        	block = world.getBlockAt(dx, dy, dz);
	
							            /* look for cobblestone*/
							            if(block.getTypeId() == 4)
							            {
											
							            	rand = generator.nextInt(100) + 1;
	
							            	if(rand > cs_chance)
							            	{
							            		block.setTypeId(48);
							            		
							            		for (int dx2 = (dx-1); dx2 < (dx+1); dx2++) 
							            		{
												    for (int dy2 = (dy-1); dy2 < (dy+1); dy2++) 
												    {
												        for (int dz2 = (dz-1); dz2 < (dz+1); dz2++) 
												        {
												            Block block2 = world.getBlockAt(dx2, dy2, dz2);
	
											            	rand2 = generator.nextInt(100) + 1;
											            	if(rand2 > cs_nearbychance)
											            	{
													            if(block.getTypeId() == 4)
													            {
													            	block2.setTypeId(48);
													            }
											            	}
												        }
												    }
							            		}
							            	}
							            }
							            /* look for clean stonebricks 98-0*/
							            else if(block.getTypeId() == 98 && block.getData() == (byte)0)
							            {
							            	Arrays.sort(coords);
							            	if(Arrays.binarySearch(coords,dx+","+dy+","+dz) < 0)
							            	{
								            	rand = generator.nextInt(100) + 1;
								            											
								            	if(rand > sb_chance)
								            	{
									            	rand2 = generator.nextInt(100) + 1;
		
									            	if(rand2 > sb_mossychance)
									            	{
									            		block.setTypeId(0);
									            		block.setTypeId(98);
									            		block.setData((byte)1);
									            		coords[i] = dx+","+dy+","+dz;
									            		i++;
									            		
									            		for (int dx2 = (dx-1); dx2 < (dx+1); dx2++) 
									            		{
														    for (int dy2 = (dy-1); dy2 < (dy+1); dy2++) 
														    {
														        for (int dz2 = (dz-1); dz2 < (dz+1); dz2++) 
														        {
														            Block block2 = world.getBlockAt(dx2, dy2, dz2);
														            
													            	rand3 = generator.nextInt(100) + 1;
													            	if(rand3 > sb_nearbymossychance)
													            	{
														            	Arrays.sort(coords);
															            if(block2.getTypeId() == 98 && block2.getData() == (byte)0 && Arrays.binarySearch(coords,dx2+","+dy2+","+dz2) < 0)
															            {
															            	block2.setTypeId(0);
															            	block2.setTypeId(98);
															            	block2.setData((byte)1);
														            		coords[i] = dx2+","+dy2+","+dz2;
														            		i++;
															            }
													            	}
														        }
														    }
									            		}
									            	}
									            	else
									            	{
									            		block.setTypeId(0);
									            		block.setTypeId(98);
									            		block.setData((byte)2);
									            		coords[i] = dx+","+dy+","+dz;
									            		i++;
									            		
									            		for (int dx2 = (dx-1); dx2 < (dx+1); dx2++) 
									            		{
														    for (int dy2 = (dy-1); dy2 < (dy+1); dy2++) 
														    {
														        for (int dz2 = (dz-1); dz2 < (dz+1); dz2++) 
														        {
														            Block block2 = world.getBlockAt(dx2, dy2, dz2);
														            
													            	rand3 = generator.nextInt(100) + 1;
													            	if(rand3 > sb_nearbybrickedchance)
													            	{
														            	Arrays.sort(coords);
															            if(block2.getTypeId() == 98 && block2.getData() == (byte)0 && Arrays.binarySearch(coords,dx2+","+dy2+","+dz2) < 0)
															            {
															            	block2.setTypeId(0);
															            	block2.setTypeId(98);
															            	block2.setData((byte)2);
														            		coords[i] = dx2+","+dy2+","+dz2;
														            		i++;
															            }
													            	}
														        }
														    }
									            		}
									            	}
								            	}
							            	}
							            }
							        }
							    }
							}
							player.sendMessage(ChatColor.GREEN + "[" + plugdisc.getName() + "] weathering complete.");
			            	succeed = true;
						}
						catch(NumberFormatException e)
						{
							player.sendMessage(ChatColor.RED + "[" + plugdisc.getName() + "] Gib bitte eine Zahl als Radius an.");
							succeed = false;
						}
						catch(Exception e)
						{
							log.info(plugdisc.getName() + " " + plugdisc.getVersion() + " Error in the try/catch block of the Preset saving command.");
							e.printStackTrace();
							succeed = false;
						}
					}
					else
					{
						player.sendMessage(ChatColor.RED + "[" + plugdisc.getName() + "] Du hast leider keine Rechte.");
						succeed = true;
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "[" + plugdisc.getName() + "] Gib bitte eine Radius an.");
					succeed = false;
				}
			}
		}
		else
		{
			log.info("[" + plugdisc.getName() + "] the /ws commands can only be used by a Player.");
			succeed = true;
		}
		
		return succeed;
	}
	
	public void loadConfig()
	{
		config.addDefault("CobbleStone.Chance", 40);
		config.addDefault("CobbleStone.nearby_Chance", 30);
		config.addDefault("Stonebricks.Chance", 30);
		config.addDefault("Stonebricks.mossy_Chance", 50);
		config.addDefault("Stonebricks.nearby_mossyChance", 30);
		config.addDefault("Stonebricks.nearby_brickedChance", 30);
		config.options().copyDefaults(true);
		saveConfig();
	}
}
