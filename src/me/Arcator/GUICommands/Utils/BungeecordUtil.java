package me.Arcator.GUICommands.Utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.Arcator.GUICommands.Main;

//Util code used from CommandNPC, all rights reserved to the author.
public class BungeecordUtil {
	
	public static void setupUtil() {
		JavaPlugin plugin = Main.getInstance();
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
	}

	public static void disableUtil() {
		JavaPlugin plugin = Main.getInstance();
		plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
	}

	public static void sendPlayerToServer(Player bplayer, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);
		bplayer.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
	}
}
