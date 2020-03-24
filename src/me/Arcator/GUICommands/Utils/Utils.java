package me.Arcator.GUICommands.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);

	}

	@SuppressWarnings({})
	public static ItemStack createItem(Inventory inv, Material material, int amount, int invSlot, String displayName,
			String... loreString) {

		ItemStack item;

		item = new ItemStack(material, amount);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.chat(displayName));
		List<String> lore = new ArrayList<String>();
		for (String s : loreString) {
			lore.add(Utils.chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(invSlot - 1, item);
		return item;

	}

}
