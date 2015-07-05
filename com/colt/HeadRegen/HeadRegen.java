package com.colt.HeadRegen;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class HeadRegen extends JavaPlugin implements Listener {
	
	ArrayList<ItemStack> skulls = new ArrayList<ItemStack>();
	List<String> lore = new ArrayList<String>();
	String noperms;
	
	public void onEnable() {
	    Bukkit.getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
	}
	
	public void onDisable() {
		skulls.clear();
		lore.clear();
	}
	
	@EventHandler
	public void click(PlayerInteractEvent e) {
		Player p = e.getPlayer();
    	if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK && skulls.contains(e.getItem())) {
			if(p.hasPermission("headregen.use")) {
				Inventory inv = p.getInventory();		
				inv.removeItem(e.getItem());
				skulls.remove(e.getItem());
    			p.setHealth(p.getHealth() + getConfig().getDouble("Hearts"));
			} else {
	    		noperms = ChatColor.translateAlternateColorCodes('&', getConfig().getString("noperms"));
	    		p.sendMessage(noperms);
			}
    	}
    }
	
    @EventHandler
    public void kill(PlayerDeathEvent e) {
    	Player p = (Player)e.getEntity();
    	List<String> worlds = getConfig().getStringList("worlds");
    	for(String w : worlds) {
			World world = Bukkit.getWorld(w);
			if(p.getWorld().equals(world)) {
    			if(p.hasPermission("headregen.use")) {
    				lore.clear();
    				lore.add(ChatColor.AQUA + "Right Click for a +" + ChatColor.GREEN + "" + getConfig().getDouble("Hearts") + "" + ChatColor.AQUA + " Heart Regen!");
    				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
    				SkullMeta meta = (SkullMeta)skull.getItemMeta();
    				meta.setOwner(p.getName());
    				meta.setDisplayName(p.getName());
    				meta.setLore(lore);
    				skull.setItemMeta(meta);
    				skulls.add(skull);
    				p.getWorld().dropItemNaturally(p.getLocation(), skull);
    			} else {
    	    		noperms = ChatColor.translateAlternateColorCodes('&', getConfig().getString("noperms"));
    	    		p.sendMessage(noperms);
    			}
    		}
    	}
    }
}
