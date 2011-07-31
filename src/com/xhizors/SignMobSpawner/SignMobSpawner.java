package com.xhizors.SignMobSpawner;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class SignMobSpawner extends JavaPlugin{
	
	private final SignMobSpawnerBlockListener blockListener = new SignMobSpawnerBlockListener(this);
	private PermissionHandler permissionHandler = null;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static String name;
	public ArrayList<String> worlds;
	private PluginManager pm;
	public LWC lwc = null;
	
	public void onEnable() {
		pm = getServer().getPluginManager();
		PluginDescriptionFile pdf = this.getDescription();
		name = pdf.getName();
		setupPermissions();
		log.info("["+name+"] Loaded");
		Plugin lwcPlugin = getServer().getPluginManager().getPlugin("LWC");
		if(lwcPlugin != null) {
		    lwc = ((LWCPlugin) lwcPlugin).getLWC();
		}
		
		pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Monitor, this);
	}
	
	public void onDisable() {
		log.info("["+name+"] is now disabled.");
	}
	
	private void setupPermissions() {
	    if (permissionHandler != null) {
	        return;
	    }
	    
	    Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
	    
	    if (permissionsPlugin == null) {
	        log.warning("["+name+"] Permissions System not found. Disabling plugin.");
	        setEnabled(false);
	        return;
	    }
	    
	    permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	}
	
	public PermissionHandler getPermissionHandler() {
		return permissionHandler;
	}
	
	public boolean hasPermission(CommandSender sender, String permission) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!permissionHandler.has(player, permission)) {
				sender.sendMessage("You do not have permission for this.");
				return false;
			}
		}
		return true;
	}
	
	public boolean hasPermission(String world, String name, String permission) {
		if (!permissionHandler.has(world, name, permission)) {
			return false;
		}
		return true;
	}
}
