package me.Arcator.GUICommands;

import org.bukkit.plugin.java.JavaPlugin;

import me.Arcator.GUICommands.Commands.GUICommands;
import me.Arcator.GUICommands.Listeners.CommandProcess;
import me.Arcator.GUICommands.Listeners.InventoryClick;
import me.Arcator.GUICommands.Listeners.JoinandClickItem;
import me.Arcator.GUICommands.Utils.BungeecordUtil;
import me.Arcator.GUICommands.Utils.Utils;
import me.Arcator.GUICommands.bStats.Metrics;

public class Main extends JavaPlugin {

	private static JavaPlugin instance;

	public void onEnable() {

		this.saveResource("example.yml", false);

		systemLog("Registering classes.");
		new InventoryClick(this);
		new CommandProcess(this);
		new JoinandClickItem(this);
		new GUICommands(this);

		systemLog("Setting up bungee channel.");
		// Bungee things!
		Main.instance = this;
		BungeecordUtil.setupUtil();
		systemLog("Bungee setup complete. If you aren't running bungee, ignore this.");

		systemLog("Saving config...");
		saveDefaultConfig();

		systemLog("Setting up bStats.");
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);

		systemLog("Plugin successfully loaded!");

	}

	public void systemLog(String s) {
		System.out.println(Utils.chat("[" + this.getName() + "] " + s));
	}

	public void onDisable() {
		systemLog(" Disabling Bungee channel");
		BungeecordUtil.disableUtil();
		systemLog(" Bungee channel disabled");
		systemLog(" Plugin disabled successfully");
	}

	// For BungeecordUtil.java
	public static Main getInstance() {
		return (Main) instance;
	}

}
