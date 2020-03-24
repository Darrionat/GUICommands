package me.Arcator.GUICommands.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Arcator.GUICommands.Utils.Utils;

public class OpenGUI {

	public static Inventory inv;

	public Inventory GUI(String inventory_name, int inv_rows, Player p, JavaPlugin plugin) {
		inv = Bukkit.createInventory(null, inv_rows * 9);
		Inventory toReturn = Bukkit.createInventory(null, inv_rows * 9, Utils.chat(inventory_name));
		Utils.createItem(inv, Material.SAND, 1, 1, "Test", "");
		toReturn.setContents(inv.getContents());
		return toReturn;

	}

	public static void clicked(Player p, int slot, ItemStack clickedItem, Inventory inv, JavaPlugin plugin) {
	}

}
