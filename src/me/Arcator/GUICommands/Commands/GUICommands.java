package me.Arcator.GUICommands.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Arcator.GUICommands.Main;
import me.Arcator.GUICommands.Utils.Utils;


public class GUICommands implements CommandExecutor{
	private Main plugin;

	public GUICommands(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("guicommands").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length > 0) {
			if (!(args[0].equalsIgnoreCase("reload"))) {
				sender.sendMessage(Utils.chat("&cAvailable commands"));
				sender.sendMessage(Utils.chat("&e/guicommands - Sends this message"));
				sender.sendMessage(Utils.chat("&e/guicommands reload - Reloads the config"));
				return true;
			}
			if (!(sender instanceof Player)) {
				Bukkit.getPluginManager().getPlugin(plugin.getName()).reloadConfig();
				sender.sendMessage(Utils.chat("&a" + plugin.getName() + "'s config reloaded!"));
				return true;
			}
			Player p = (Player) sender;
			if (!p.hasPermission("guicommands.reload")) {
				p.sendMessage(Utils.chat("&cYou do not have the permission 'guicommands.reload'"));
				return true;
			}
			Bukkit.getPluginManager().getPlugin(plugin.getName()).reloadConfig();
			p.sendMessage(Utils.chat("&a" + plugin.getName() + "'s config reloaded!"));
			return true;

		}
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Utils.chat("&cAvailable commands"));
				sender.sendMessage(Utils.chat("&e/guicommands - Sends this message"));
				sender.sendMessage(Utils.chat("&e/guicommands reload - Reloads the config"));
				return true;
			}
			Player p = (Player) sender;
			p.sendMessage(Utils.chat("&cAvailable commands"));
			p.sendMessage(Utils.chat("&e/guicommands - Sends this message"));
			p.sendMessage(Utils.chat("&e/guicommands reload - Reloads the config"));
				return true;
			
		}
		return false;
	}
}
