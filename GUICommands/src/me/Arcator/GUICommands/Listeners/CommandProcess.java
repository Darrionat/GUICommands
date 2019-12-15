package me.Arcator.GUICommands.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.Arcator.GUICommands.Main;

public class CommandProcess implements Listener {

	private Main plugin;

	public CommandProcess(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void commandSent(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String sentcommand = e.getMessage();
		if (plugin.getConfig().getString("GUICommand").equals(sentcommand)) {
			p.openInventory(Main.GUI(p, plugin));
			e.setCancelled(true);
			return;
		}
		if (plugin.getConfig().getString("GUICloseCommand").equals(sentcommand)) {
			p.closeInventory();
			e.setCancelled(true);
			return;
		}
	}
}
