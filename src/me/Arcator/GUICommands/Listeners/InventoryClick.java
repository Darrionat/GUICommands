package me.Arcator.GUICommands.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import me.Arcator.GUICommands.Main;
import me.Arcator.GUICommands.Utils.Utils;

public class InventoryClick implements Listener {
	private Main plugin;

	public InventoryClick(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		String title = e.getView().getTitle();
		if (title.equals("name")) {
			e.setCancelled(true);
			// Main.clicked(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), plugin);
		}

		ItemMeta clickeditem = e.getCurrentItem().getItemMeta();
		if (clickeditem == null) {
			return;
		}
		String name = clickeditem.getDisplayName();
		String GUIItemName = Utils.chat(plugin.getConfig().getString("GUIItemName"));

		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if (!name.equalsIgnoreCase(GUIItemName)) {
			return;
		}
		if (plugin.getConfig().getBoolean("PlayerCanMoveItem") == true) {
			return;
		}
		e.setCancelled(true);
		return;
	}
}