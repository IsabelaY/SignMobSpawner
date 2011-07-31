package com.xhizors.SignMobSpawner;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.CreatureType;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.griefcraft.model.Protection;

public class SignMobSpawnerBlockListener extends BlockListener{
	
	private SignMobSpawner plugin;
	
	public SignMobSpawnerBlockListener(SignMobSpawner plugin) {
		this.plugin = plugin;
	}
	
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		if (event.getNewCurrent() > 0 && event.getOldCurrent() <= 0) {
			Block block = event.getBlock();
			checkMobSpawn(block.getRelative(BlockFace.EAST));
			checkMobSpawn(block.getRelative(BlockFace.WEST));
			checkMobSpawn(block.getRelative(BlockFace.NORTH));
			checkMobSpawn(block.getRelative(BlockFace.SOUTH));
			checkMobSpawn(block.getRelative(BlockFace.UP));
		}
	}
	
	public void checkMobSpawn(Block relative) {
		if (relative.getTypeId() == 68 || relative.getTypeId() == 63) {
			Sign sign = (Sign) relative.getState();
			Protection protection = plugin.lwc.findProtection(relative);
			if (protection == null) return;
			String playerName = protection.getOwner();
			World world = relative.getWorld();
			if (!plugin.hasPermission(world.getName(), playerName, "SignMobSpawner.PlaceSpawner")) {
				return;
			}
			if (sign.getLine(0).contains("[Mob]")) {
				String mobName = sign.getLine(1);
				int num;
				try {
					num = Integer.parseInt(sign.getLine(2));
					if (num > 50) num = 50;
				} catch (NumberFormatException e) {
					num = 1;
				}
				if (num <= 0) num = 1;
				CreatureType cType = CreatureType.fromName(mobName);
				if (cType != null) {
					while (relative.getTypeId() != 0) {
						relative = relative.getRelative(BlockFace.UP);
					}
					for (int i = 0; i < num; i++) {
						world.spawnCreature(relative.getLocation(), cType);
					}
				}
			}
		}
	}
}
