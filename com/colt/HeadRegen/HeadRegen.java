package com.colt.HeadRegen;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class HeadRegen extends JavaPlugin {
	
	public void onEnable() {
		saveConfig();
	}
	
	public void onDisable() {
		skulls.clear();
		lore.clear();
	}
	
	List<String> lore = new ArrayList<String>();
	ArrayList<ItemStack> skulls = new ArrayList<ItemStack>();
	
	@EventHandler
	public void click(PlayerInteractEvent e) {
		Player p = e.getPlayer();
    	if(!p.hasPermission("headregen.use")) {
    		String msg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("noperms"));
    		p.sendMessage(msg);
    	} else {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(skulls.contains(e.getItem())) {
				Inventory inv = p.getInventory();		
				inv.removeItem(e.getItem());
				skulls.remove(e.getItem());
				p.setHealth(p.getHealth() + getConfig().getDouble("Hearts"));
			}
		}
    	}
	}
	
    @EventHandler
    public void kill(PlayerDeathEvent e) {
    	Player p = (Player)e.getEntity();
    	if(p.hasPermission("headregen.use")) {
    	lore.clear();
    	lore.add(ChatColor.AQUA + "Right Click for a +" + ChatColor.GREEN + "" + getConfig().getDouble("Hearts") + "" + ChatColor.AQUA + " Heart Regen!");
    	ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
    	SkullMeta meta = (SkullMeta)skull.getItemMeta();
    	meta.setOwner(p.getName());
    	meta.setDisplayName(p.getName());
    	meta.setLore(lore);
    	skull.setItemMeta(meta);
    	skulls.add(skull);
    	p.getWorld().dropItemNaturally(p.getLocation(), skull);
    }
    }
}
