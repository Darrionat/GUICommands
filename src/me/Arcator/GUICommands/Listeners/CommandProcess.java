package me.Arcator.GUICommands.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.Arcator.GUICommands.Main;
import me.Arcator.GUICommands.Files.FileManager;
import me.Arcator.GUICommands.GUI.OpenGUI;

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
		String[] fileList = plugin.getDataFolder().list();
		for (String s : fileList) {
			String fileName = s.replace(".yml", "");
			if (fileName.equalsIgnoreCase("config")) {
				continue;
			}
			FileManager fileManager = new FileManager(plugin);
			FileConfiguration guiConfig = fileManager.getDataConfig(fileName);
			if (!guiConfig.getString("OpenCommand").equalsIgnoreCase(sentcommand)) {
				continue;
			}
			OpenGUI openGUI = new OpenGUI();
			p.openInventory(openGUI.GUI("test", 1, p, plugin));
			e.setCancelled(true);

		}
	}
}
